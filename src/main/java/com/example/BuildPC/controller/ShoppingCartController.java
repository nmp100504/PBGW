package com.example.BuildPC.controller;

import com.example.BuildPC.model.CartItem;
import com.example.BuildPC.service.AdminService;
import com.example.BuildPC.service.ShoppingCartService;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.UserService;
import com.example.BuildPC.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/cart")
    public String showShoppingCart(Model model) {
        Optional<User> currentUserOpt = userService.getCurrentUser();

        if (currentUserOpt.isPresent()) {
            User user = currentUserOpt.get();
            List<CartItem> cartItems = shoppingCartService.listCartItems(user);

            model.addAttribute("cartItems", cartItems);
            return "LandingPage/cart";
        } else {
            // Handle case where user is not found (though ideally, should not happen)
            return "redirect:/login"; // Redirect to login page or handle appropriately
        }
    }

    // Method to get the current logged-in user
}
