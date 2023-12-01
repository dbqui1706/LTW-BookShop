package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.dto.WishlistItemDto;
import com.example.bookshopwebapplication.network.WishlistItemRequest;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.entities.WishListItem;
import com.example.bookshopwebapplication.message.Message;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.service.WishlistItemService;
import com.example.bookshopwebapplication.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistItemService wishlistItemService = new WishlistItemService();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin người dùng
        UserDto user = (UserDto) request.getSession().getAttribute("currentUser");
        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (user != null) {
            // Lấy danh sách mục yêu thích của người dùng
            List<WishlistItemDto> wishlistItems = wishlistItemService.getByUserId(user.getId());

            // Lấy thông tin chi tiết của từng sản phẩm trong danh sách yêu thích
            for (WishlistItemDto wishlistItem : wishlistItems.isEmpty() ? new LinkedList<WishlistItemDto>() : wishlistItems) {
                ProductDto product = productService.getById(wishlistItem.getProduct().getId()).orElseGet(ProductDto::new);
                wishlistItem.setProduct(product);
            }
            request.setAttribute("wishlistItems", wishlistItems);
        }
        request.getRequestDispatcher("/WEB-INF/views/client/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        //xóa mục yêu thích
        wishlistItemService.delete(new Long[]{id});
        response.sendRedirect(request.getContextPath() + "/wishlist");
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đọc dữ liệu yêu thích từ yêu cầu dưới định dạng JSON
        WishlistItemRequest wishlistItemRequest = JsonUtils.get(request, WishlistItemRequest.class);
        String successMessage = "Đã thêm sản phẩm vào danh sách yêu thích thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        // Hàm thực hiện khi thành công
        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);

        // Hàm thực hiện khi thất bại
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        // Tạo đối tượng mục yêu thích từ dữ liệu yêu cầu
        WishlistItemDto wishlistItem = new WishlistItemDto();
        wishlistItem.setUser(
                userService.getById(wishlistItemRequest.getUserId()).get()
        );
        wishlistItem.setProduct(
                productService.getById(wishlistItemRequest.getProductId()).get()
        );

        // thêm mục yêu thích
        Optional<WishlistItemDto> res = wishlistItemService.insert(wishlistItem);
        if (res.isPresent()) doneFunction.run();
        else failFunction.run();
    }
}