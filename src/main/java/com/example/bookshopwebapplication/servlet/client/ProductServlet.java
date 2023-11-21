package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.dto.ProductReviewDto;
import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.ProductReview;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.*;
import com.example.bookshopwebapplication.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final ProductReviewService productReviewService = new ProductReviewService();
    private final WishlistItemService wishlistItemService = new WishlistItemService();
    private static final int PRODUCT_REVIEWS_PER_PAGE = 2;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy id của product và đối tượng product từ database theo id này
        long id = Long.parseLong(request.getParameter("id"));
        Optional<ProductDto> productFromServer = productService.getById(id);
        if (id > 0L && productFromServer.isPresent()) {
            Optional<CategoryDto> categoryFromServer = categoryService.getByProductId(id);
            CategoryDto category = categoryFromServer.orElseGet(CategoryDto::new);

            // Lấy product từ productFromServer
            ProductDto product = productFromServer.get();
            product.setDescription(TextUtils.toParagraph(
                    Optional.ofNullable(product.getDescription()).orElse(""))
            );

            // Lấy tổng số đánh giá (productReview) của sản phẩm
            int totalProductReviews = productReviewService.countByProductId(id);

            // Tính tổng số trang để phân trang phần đánh giá
            int totalPagesOfProductReviews = totalProductReviews / PRODUCT_REVIEWS_PER_PAGE;
            if (totalProductReviews % PRODUCT_REVIEWS_PER_PAGE != 0) {
                totalPagesOfProductReviews++;
            }

            // Lấy trang đánh giá hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang)
            // thì gán bằng 1
            String pageReviewParam = Optional.ofNullable(request.getParameter("pageReview")).orElse("1");
            int pageReview = Integer.parseInt(pageReviewParam);
            if (pageReview < 1 || pageReview > totalPagesOfProductReviews) {
                pageReview = 1;
            }

            // Tính mốc truy vấn (offset)
            int offset = (pageReview - 1) * PRODUCT_REVIEWS_PER_PAGE;

            // Lấy các productReview theo productId
            List<ProductReviewDto> productReviews = productReviewService.getOrderedPartByProductId(
                    PRODUCT_REVIEWS_PER_PAGE, offset, "createdAt", "DESC", id
            );

            productReviews.forEach(productReview -> productReview.setContent(
                    TextUtils.toParagraph(productReview.getContent())));

            // Lấy tổng cộng số sao đánh giá của sản phẩm
            int sumRatingScores = productReviewService.sumRatingScoresByProductId(id);

            // Tính số sao đánh giá trung bình
            int averageRatingScore = (totalProductReviews == 0) ? 0 : (sumRatingScores / totalProductReviews);

            // Lấy các sản phẩm liên quan
            List<ProductDto> relatedProducts = productService.getRandomPartByCategoryId(
                    4, 0, category.getId()
            );

            // Kiểm tra có phải là sản phẩm yêu thích
            Optional<UserDto> currentUser = Optional.ofNullable(
                    (UserDto) request.getSession().getAttribute("currentUser")
            );
            int isWishlistItem = currentUser.isEmpty() ? 0 :
                    wishlistItemService.countByUserIdAndProductId(currentUser.get().getId(), id);

            request.setAttribute("category", category);
            request.setAttribute("product", product);
            request.setAttribute("totalProductReviews", totalProductReviews);
            request.setAttribute("productReviews", productReviews);
            request.setAttribute("totalPagesOfProductReviews", totalPagesOfProductReviews);
            request.setAttribute("pageReview", pageReview);
            request.setAttribute("averageRatingScore", averageRatingScore);
            request.setAttribute("relatedProducts", relatedProducts);
            request.setAttribute("isWishlistItem", isWishlistItem);
            request.getRequestDispatcher("/WEB-INF/views/client/productView.jsp").forward(request, response);
        } else {
            // Nếu id không phải là số nguyên hoặc không hiện diện trong bảng product
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
