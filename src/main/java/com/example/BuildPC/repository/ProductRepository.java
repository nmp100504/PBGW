package com.example.BuildPC.repository;

import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductName(String productName);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByProductStatus(boolean Status);
    List<Product> findByProductNameContaining(String productName);
    Product findByProductName(String productName);
    List<Product> findByProductSalePriceBetween(int minPrice, int maxPrice);
    List<Product> findByOrderByProductSalePriceAsc();
    List<Product> findByOrderByProductSalePriceDesc();
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %?1% OR p.category.categoryName LIKE %?1%")
    List<Product> searchByProductNameOrCategoryName(String productNameOrCategoryName);
    @Query("SELECT c FROM Product c WHERE c.category.categoryName LIKE %?1%")
    List<Product> searchCategoryByName(String categoryName);
    long countByProductStatus(boolean Status);

    @Query("SELECT p FROM Product p WHERE (p.productName LIKE %?1% OR p.category.categoryName LIKE %?1%) AND p.productStatus = ?2")
    List<Product> searchByProductNameOrCategoryNameAndStatus(String productNameOrCategoryName, boolean status);

    @Query("SELECT p FROM Product p ORDER BY p.unitsInOrder DESC")
    List<Product> findTop10ByUnitsInOrderDesc();
}
