package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.service.OrderService;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "auth/login_page";
    }


    @Autowired
    ProductService productService;
    @GetMapping({"/", "/homepage"})
    public String showLandingPage(Model model) {
        List<Category> categoryList = categoryService.findAll();
        List<Product> productList = productService.listActiveProduct(true);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("productList",productList);
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

}
