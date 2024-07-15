package com.example.BuildPC.service;

import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.dto.CategoryDto;
import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> findAll();
    void create(ProductDto productDto);
    Boolean update(Product product);
    Boolean delete(int id);
    List<Product> listByCategory(int id);
    List<Product> listActiveProduct(boolean status);
    Product findById(int id);
    Product findProductById(int id);
    void updateProduct(Product product);
    void deleteProduct(int id);
    void deActivateProduct(int id);
    boolean existsByProductName(String productName);
    //boolean existsByProductNameAndProductId(String productName, Integer productId);
    List<Product> findByProductNameContaining(String productName);
    List<Product> findByProductSalePriceBetween(int minPrice, int maxPrice);
    List<Product> findByOrderByProductSalePriceAsc();
    List<Product> findByOrderByProductSalePriceDesc();
    List<Product> searchByProductNameOrCategoryName(String productNameOrCategoryName);
    List<Product> searchCategoryName(String categoryName);
    long countTotalProducts();
    long countActiveProducts();
    long countInActiveProducts();
    List<Product> findActiveProducts();
    List<Product> findInActiveProducts();
    List<Product> searchByProductNameOrCategoryNameAndStatus(String searchByProductNameOrCategoryNameAndStatus, boolean status);


}
