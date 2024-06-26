package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.WishlistItem;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.service.UserService;
import com.example.BuildPC.service.WishlistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @GetMapping("/wishlist")
    public String showWishlist(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> currentUserOpt = userService.findByEmail(userDetails.getEmail());
            if (currentUserOpt.isPresent()) {
                User user = currentUserOpt.get();
                List<WishlistItem> wishlistItems = wishlistService.listWishlistItems(user);

                model.addAttribute("wishlistItems", wishlistItems);
                return "LandingPage/wishlist";
            }
        }
        return "auth/login_page";
    }

    @PostMapping("/add-to-wishlist")
    public String addItemToWishlist(
            @RequestParam("id") int productId,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        if (userDetails != null) {
            Optional<User> userOpt = userService.findByEmail(userDetails.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Product product = productService.findById(productId);
                    // Check if the product is already in the wishlist
                    if (!wishlistService.isProductInWishlist(user, product)) {
                        WishlistItem wishlistItem = new WishlistItem();
                        wishlistItem.setUser(user);
                        wishlistItem.setProduct(product);
                        wishlistItem.setQuantity(quantity);

                        wishlistService.save(wishlistItem);
                    }

                    return "redirect:" + request.getHeader("referer");
            }
        }
        return "redirect:/login";
    }


    @PostMapping("/remove-from-wishlist")
    public String deleteWishlistItem(
            @RequestParam("productId") int productId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        if (userDetails != null) {
            Optional<User> userOpt = userService.findByEmail(userDetails.getEmail());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Product product = productService.findById(productId);

                // Find the WishlistItem for the given user and product
                Optional<WishlistItem> wishlistItemOpt = wishlistService.findWishlistItem(user, product);
                if (wishlistItemOpt.isPresent()) {
                    WishlistItem wishlistItem = wishlistItemOpt.get();

                    // Remove the found WishlistItem
                    wishlistService.remove(wishlistItem);
                }
            }
        }
        return "redirect:" + request.getHeader("referer");
    }

}
