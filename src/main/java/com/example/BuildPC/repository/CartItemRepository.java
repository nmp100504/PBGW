package com.example.BuildPC.repository;

import com.example.BuildPC.models.CartItem;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    public List<CartItem> findByUser(User user);
    public CartItem findByUserAndProduct(User user, Product product);
}
