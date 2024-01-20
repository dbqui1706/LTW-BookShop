package com.example.bookshopwebapplication.servlet.admin.product;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.ImageUtils;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/productManager/delete")
public class DeleteProduct extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<ProductDto> productFromServer = Protector.of(() -> productService.getById(id)).get(Optional::empty);

        if (productFromServer.isPresent()) {
            String successMessage = String.format("Xóa sản phẩm #%s thành công!", id);
            String errorMessage = String.format("Xóa sản phẩm #%s thất bại!", id);

            Optional<CategoryDto> categoryFromServer = Protector.of(() -> categoryService.getByProductId(id)).get(Optional::empty);

            Protector.of(() -> {
                        productService.delete(new Long[]{id});
                        Optional.ofNullable(productFromServer.get().getImageName()).ifPresent(ImageUtils::delete);
                    })
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }

        response.sendRedirect(request.getContextPath() + "/admin/productManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}