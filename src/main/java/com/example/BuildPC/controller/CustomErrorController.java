package com.example.BuildPC.controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public String handleError(HttpServletRequest request) {
        // Get the HTTP error status code
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // Handle different error codes
        if (statusCode != null) {
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403"; // Thymeleaf template for 403 Forbidden error
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404"; // Thymeleaf template for 404 Not Found error
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500"; // Thymeleaf template for 500 Internal Server Error
            }
        }

        // For other errors, display a generic error page
        return "error/error"; // Thymeleaf template for other errors
    }

    public String getErrorPath() {
        return ERROR_PATH;
    }
}
