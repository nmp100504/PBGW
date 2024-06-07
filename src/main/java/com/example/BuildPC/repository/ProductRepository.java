package com.example.BuildPC.repository;

import com.example.BuildPC.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
