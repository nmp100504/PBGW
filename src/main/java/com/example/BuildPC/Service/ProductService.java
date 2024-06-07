package com.example.BuildPC.Service;

import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> findAll();
    void create(ProductDto productDto);
    Product findById(int id);
    Boolean update(Product product);
    Boolean delete(int id);

}
