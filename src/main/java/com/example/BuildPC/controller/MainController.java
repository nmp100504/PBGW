package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.*;
import com.example.BuildPC.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        List<Product> reverseProductList = productService.listActiveProduct(true);
        Collections.reverse(reverseProductList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("reverseProductList", reverseProductList);
        return "LandingPage/index_1";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account_information";
            }
        return "LandingPage/index_1";
    }



    @GetMapping("/change-password")
    public String changePassword(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> currentUser = userService.findByEmail(email);
            Optional<User> user = userService.findByEmail(currentUser.get().getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/change_password";
            }
        return "redirect:/login";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> userOptional = userService.findByEmail(email);
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
                userService.updateUser(user, newPassword);
                return "redirect:/account";
            }

        return "redirect:/login";
    }

    @GetMapping("/update-user-info")
    public String editInfo(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account_information";
            }

        return "LandingPage/index_1";
    }
    @PostMapping("/update-user-info")
    public String updateUserInfo(@RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("avatar") MultipartFile avatar,
                                 Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                userService.updateUser(user, avatar);
                model.addAttribute("user", user);
                return "auth/account_information";
            }

        return "redirect:/login";
    }
}
