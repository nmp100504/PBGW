package com.example.BuildPC.controller;

import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.service.OrderService;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {


    @Autowired
    private CategoryService categoryService;
//    @GetMapping("/")
//    public String getHomePage() {
//        return "LandingPage/index_1";
//    }
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
        return "auth/index_1";
    }

    @Autowired private OrderService orderService;

}
