package com.example.BuildPC.repository;

import com.example.BuildPC.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    //    List<Product> findByProductStatus(boolean status);
    boolean existsByProductName(String productName);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByProductStatus(boolean Status);
    List<Product> findByProductNameContaining(String productName);
    List<Product> findByProductSalePriceBetween(int minPrice, int maxPrice);
    List<Product> findByOrderByProductSalePriceAsc();
    List<Product> findByOrderByProductSalePriceDesc();
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %?1%")
    List<Product> searchProductName(String productName);
}
