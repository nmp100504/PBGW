package com.example.BuildPC.repository;

import com.example.BuildPC.model.Order;
import com.example.BuildPC.model.OrderDetail;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProduct(Product product);
}
