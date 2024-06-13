package com.example.BuildPC.controller;

import com.example.BuildPC.model.CartItem;
import com.example.BuildPC.service.AdminService;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired private ShoppingCartService shoppingCartService;
    @Autowired private AdminService adminService; 

    @GetMapping("/cart")//need to have current login user
    public String showShoppingCart(Model model){
        User user = adminService.findUserById(1);
        List<CartItem> cartItems = shoppingCartService.listCartItems(user);

        model.addAttribute("cartItems", cartItems);
        return "LandingPage/cart";
    }
}
