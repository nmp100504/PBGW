package com.example.BuildPC.repository;

import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findByUser(User user);

    boolean existsByUserAndProduct(User user, Product product);

    WishlistItem findByUserAndProduct(User user, Product product);
}
