package com.example.BuildPC.controller;

import com.example.BuildPC.Service.CategoryService;
import com.example.BuildPC.Service.OrderService;
import com.example.BuildPC.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {


    @Autowired
    private CategoryService categoryService;
    @GetMapping("/")
    public String getHomePage() {

        return "LandingPage/index_1";
    }
    @GetMapping("/index")
    public String getHome() {
        return "LandingPage/auth_index";
    }

    @GetMapping("/home")
    public String showLandingPage(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);
        return "LandingPage/index_1";
    }



    @Autowired private OrderService orderService;
    @GetMapping("/ManagerDashBoard")
    public String showManagerDashBoard(Model model) {
        model.addAttribute("OrderList", orderService.listAllOrder());
        return "Manager/managerDashBoard";
    }
}
