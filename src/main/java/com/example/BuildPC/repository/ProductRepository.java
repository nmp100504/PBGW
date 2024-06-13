package com.example.BuildPC.repository;

import com.example.BuildPC.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
//    List<Product> findByProductStatus(boolean status);
    boolean existsByProductName(String productName);
    List<Product> findByCategoryId(int categoryId);
}
