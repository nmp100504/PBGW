package com.example.BuildPC.service;

import com.example.BuildPC.model.CartItem;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.CartItemRepository;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> listCartItems(User user){
        return cartItemRepository.findByUser(user);
    }

    public Integer addProduct(Integer productId, Integer quantity, User user) {
        Integer addedQuantity = quantity;

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem != null) {
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);

        return addedQuantity;
    }

    public void updateProductQuantity(Integer productId, Integer quantity, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Cart item not found for user: " + user.getEmail() + " and product ID: " + productId);
        }
    }

    public void deleteProduct(Integer productId, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new IllegalArgumentException("Cart item not found for user: " + user.getEmail() + " and product ID: " + productId);
        }
    }

}
