package com.example.BuildPC.controller;

import com.example.BuildPC.service.AdminService;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @PostMapping("/cart/add/{pid}/{qty}")
    public String addProduct(@PathVariable("pid") Integer productId, @PathVariable("qty") Integer quantity) {
        Optional<User> currentUserOpt = getCurrentUser();

        if (currentUserOpt.isPresent()) {
            User user = currentUserOpt.get();
            Integer addedQuantity = shoppingCartService.addProduct(productId, quantity, user);

            return addedQuantity + " added to your shopping cart!";
        } else {
            // Handle case where user is not found (though ideally, should not happen)
            return "User not found. Please log in again."; // Or handle appropriately
        }
    }

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return userService.findByEmail(email);
    }
}
