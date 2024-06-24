package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.service.OrderService;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "auth/login_page";
    }

    @GetMapping({"/", "/homepage"})
    public String showLandingPage(Model model) {
        List<Category> categoryList = categoryService.findAll();
        List<Product> productList = productService.listActiveProduct(true);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        return "LandingPage/index_1";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account";
            }
        }
        return "LandingPage/index_1";
    }



    @GetMapping("/change-password")
    public String changePassword(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/change_password";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {
        if (userDetails != null) {
            Optional<User> userOptional = userService.findByEmail(userDetails.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                    model.addAttribute("error", "Old password is incorrect");
                    return "auth/change_password";
                }
                if (!newPassword.equals(confirmPassword)) {
                    model.addAttribute("error", "New passwords do not match");
                    return "auth/change_password";
                }
                user.setPassword((newPassword));
                userService.updateUser(user);
                return "redirect:/account";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/update-user-info")
    public String editInfo(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account_information";
            }
        }
        return "LandingPage/index_1";
    }
    @PostMapping("/update-user-info")
    public String updateUserInfo(@RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("phone") String phone,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpServletRequest request, Model model) {
        if (userDetails != null) {
            Optional<User> userOptional = userService.findByEmail(userDetails.getEmail());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                userService.updateUser(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
                model.addAttribute("user", user);
                return "auth/account"; // Replace with the appropriate view name
            }
        }
        return "redirect:/login";
    }
}
