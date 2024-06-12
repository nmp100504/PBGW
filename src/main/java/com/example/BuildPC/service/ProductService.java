package com.example.BuildPC.service;

import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> findAll();
    void create(ProductDto productDto);
    Boolean update(Product product);
    Boolean delete(int id);
    List<Product> listByCategory(int id);
    Product findById(int id);
}
