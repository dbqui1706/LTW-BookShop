package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.EncodePassword;
import com.example.bookshopwebapplication.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/signup")
public class SignUp extends HttpServlet {
    private final UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/client/signup.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> values = new HashMap<>();
        Map<String, List<String>> violations = new HashMap<>();
        int sumOfViolations;

        values.put("username", request.getParameter("username"));
        values.put("password", request.getParameter("password"));
        values.put("fullname", request.getParameter("fullname"));
        values.put("email", request.getParameter("email"));
        values.put("phoneNumber", request.getParameter("phoneNumber"));
        values.put("gender", request.getParameter("gender"));
        values.put("address", request.getParameter("address"));
        values.put("policy", request.getParameter("policy"));

        // Kiểm tra các parameter, lưu các vi phạm (nếu có) vào map violations
        Optional<UserDto> userFromServer = userService.getByUsername(values.get("username"));
        violations.put("usernameViolations", Validator.of(values.get("username"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isNotExistent(userService.getAllUsername().contains(values.get("username"))
                        ? false : true, "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(values.get("password"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .toList());
        violations.put("fullnameViolations", Validator.of(values.get("fullname"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("emailViolations", Validator.of(values.get("email"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^[^@]+@[^@]+\\.[^@]+$", "email")
                .toList());
        violations.put("phoneNumberViolations", Validator.of(values.get("phoneNumber"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^\\d{10,11}$", "số điện thoại")
                .toList());
        violations.put("genderViolations", Validator.of(values.get("gender"))
                .isNotNull()
                .toList());
        violations.put("addressViolations", Validator.of(values.get("address"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("policyViolations", Validator.of(values.get("policy"))
                .isNotNull()
                .toList());

        // Tính tổng các vi phạm sau kiểm tra (nếu có)
        sumOfViolations = violations.values().stream().mapToInt(List::size).sum();
        String successMessage = "Đã đăng ký thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        // Khi không có vi phạm trong kiểm tra các parameter
        if (sumOfViolations == 0) {
            UserDto user = new UserDto(
                    0L,
                    values.get("username"),
                    EncodePassword.hash(values.get("password")),
                    values.get("fullname"),
                    values.get("email"),
                    values.get("phoneNumber"),
                    Integer.parseInt(values.get("gender")),
                    values.get("address"),
                    "CUSTOMER"
            );
            Optional<UserDto> userSignUp = userService.insert(user);
            if (userSignUp.isPresent()) {
                request.setAttribute("successMessage", successMessage);
            } else {
                request.setAttribute("errorMessage", errorMessage);
            }
        } else {
            // Khi có vi phạm
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
        }
        request.getRequestDispatcher("/WEB-INF/views/client/signin.jsp").forward(request, response);
    }
}
