package com.example.BuildPC.controller;

import com.example.BuildPC.Service.CategoryService;
import com.example.BuildPC.Service.OrderService;
import com.example.BuildPC.Service.ProductService;
import com.example.BuildPC.models.Category;
import com.example.BuildPC.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    ProductService productService;
    @GetMapping("")
    public String showLandingPage(Model model) {
        List<Category> categoryList = categoryService.findAll();
        List<Product> productList = productService.findAll();
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("productList",productList);
        return "LandingPage/index_1";
    }

    @Autowired private OrderService orderService;

}
