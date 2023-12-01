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
import com.example.bookshopwebapplication.utils.Paging;
import com.example.bookshopwebapplication.utils.Protector;

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
    private final static int WISH_ITEM_PER_PAGE = 4;
    private final WishlistItemService wishlistItemService = new WishlistItemService();
    private final ProductService productService = new ProductService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute("currentUser");

        if (user != null) {
            List<WishlistItemDto> wishlistItems = wishlistItemService.getByUserId(user.getId());
            int totalWishItem = wishlistItems.size();
            String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
            int page = Integer.parseInt(pageParam);

            int totalPage = Paging.totalPages(totalWishItem, WISH_ITEM_PER_PAGE);
            int offset = Paging.offset(page, totalWishItem, WISH_ITEM_PER_PAGE);
            wishlistItems = wishlistItemService.getOrderedPartByUserId(user.getId(),
                    WISH_ITEM_PER_PAGE, offset, "createdAt", "DESC"
            );
            request.setAttribute("wishlistItems", wishlistItems);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("page", page);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/wishlist.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        String successMessage = "Đã xóa sản phẩm khỏi giỏ hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";
        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);


        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (id > 0L) {
            Protector.of(() -> wishlistItemService.delete(new Long[]{id}))
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
//            response.sendRedirect(request.getContextPath() + "/wishlist");
        } else {
            failFunction.run();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WishlistItemRequest wishlistItemRequest = JsonUtils.get(request, WishlistItemRequest.class);

        String successMessage = "Đã thêm sản phẩm vào danh sách yêu thích thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        WishlistItemDto wishlistItem = new WishlistItemDto();
        wishlistItem.setUser(
                userService.getById(wishlistItemRequest.getUserId()).get()
        );
        wishlistItem.setProduct(
                productService.getById(wishlistItemRequest.getProductId()).get()
        );

        Protector.of(() -> wishlistItemService.insert(wishlistItem))
                .done(r -> doneFunction.run())
                .fail(e -> failFunction.run());
    }
}
