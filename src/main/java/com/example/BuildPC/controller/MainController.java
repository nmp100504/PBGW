package com.example.BuildPC.controller;

import com.example.BuildPC.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String showHomePage() {
        return "index";
    }

    @Autowired private OrderService orderService;
    @GetMapping("/ManagerDashBoard")
    public String showAdminDashBoard(Model model) {
        model.addAttribute("OrderList", orderService.listAllOrder());
        return "users/ManagerDashBoard";
    }
}
