package com.example.BuildPC.Service;

import com.example.BuildPC.models.CartItem;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.models.User;
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

    public Integer addProduct(Integer productiId, Integer quantity, User user){
        Integer addedQuantity = quantity;

        Product product = productRepository.findById(productiId).get();

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(cartItem != null){
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        }else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setUser(user);
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);

        return addedQuantity;
    }
}
