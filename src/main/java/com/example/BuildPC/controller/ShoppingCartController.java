package com.example.BuildPC.controller;

import com.example.BuildPC.models.CartItem;
import com.example.BuildPC.Service.AdminService;
import com.example.BuildPC.Service.ShoppingCartService;
import com.example.BuildPC.models.User;
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
