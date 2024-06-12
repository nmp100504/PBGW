package com.example.BuildPC.repository;

import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProduct(Product product);
}
