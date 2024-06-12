package com.example.BuildPC.controller;

import com.example.BuildPC.service.AdminService;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AdminService adminService;
    @PostMapping("/cart/add/{pid}/{qty}")//need current user (authentication)
    public String addProduct(@PathVariable("pid") Integer productId, @PathVariable("qty") Integer quantity){
        User user = adminService.findUserById(1);

        Integer addedQuantity = shoppingCartService.addProduct(productId, quantity, user);

        return addedQuantity + "added to your shopping cart!"; 
    }
}
