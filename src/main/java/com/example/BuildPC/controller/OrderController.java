package com.example.BuildPC.controller;

import com.example.BuildPC.Service.OrderService;
import com.example.BuildPC.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/Orders")
    public String showOrders(Model model) {
        List<Order> orderList = orderService.listAllOrder();
        model.addAttribute("orderList", orderList);
        return "Orderlist";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        orderService.removeOrderById(id);
    return "redirect:/AdminDashBoard";
    }


}
