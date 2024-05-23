package com.example.BuildPC.controller;

import com.example.BuildPC.Service.OrderService;
import com.example.BuildPC.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/Orders")
    public String showOrders(Model model) {
        List<Order> orderList = orderService.listAllOrder();
        model.addAttribute("orderList", orderList);
        return "ManagerDashBoard";
    }
}
