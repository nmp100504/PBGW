package com.example.BuildPC.repository;

import com.example.BuildPC.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
