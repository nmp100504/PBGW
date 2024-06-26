package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.*;
import com.example.BuildPC.service.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserServiceImpl userService;
//    @Qualifier("productService")

    @Autowired
    private ProductService productService;


    @GetMapping("/cart")
    public String showShoppingCart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> currentUserOpt = userService.findByEmail(userDetails.getEmail());
            if (currentUserOpt.isPresent()) {
                User user = currentUserOpt.get();
                List<CartItem> cartItems = shoppingCartService.listCartItems(user);

                // Calculate the total price
                double totalPrice = cartItems.stream()
                        .mapToDouble(item -> item.getProduct().getProductSalePrice() * item.getQuantity())
                        .sum();

                model.addAttribute("cartItems", cartItems);
                model.addAttribute("totalPrice", totalPrice);
                return "LandingPage/cart";
            }
        }
        return "auth/login_page";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(
            @RequestParam("id") Integer productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        if( userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                shoppingCartService.addProduct(productId, quantity, user.get());
                return "redirect:" + request.getHeader("referer");
            }
        }
        return "redirect:/login";
    }


    @PostMapping("/update-cart")
    public String updateCart(@RequestParam Map<String, String> allParams,
                             @AuthenticationPrincipal CustomUserDetails userDetails,
                             HttpServletRequest request) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                for (Map.Entry<String, String> entry : allParams.entrySet()) {
                    if (entry.getKey().startsWith("quantities[")) {
                        Integer productId = Integer.valueOf(entry.getKey().substring(11, entry.getKey().length() - 1));
                        Integer quantity = Integer.valueOf(entry.getValue());
                        Product product = productService.findProductById(productId);
                        if (product != null) {
                            shoppingCartService.updateProductQuantity(productId, quantity, user.get());
                        }
                    }
                }
                return "redirect:" + request.getHeader("referer");
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/delete-cart-item")
    public String deleteCartItem(@RequestParam("productId") Integer productId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 HttpServletRequest request) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                shoppingCartService.deleteProduct(productId, user.get());
                return "redirect:" + request.getHeader("referer");
            }
        }
        return "redirect:/login";
    }
}