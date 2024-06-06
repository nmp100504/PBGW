package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    List<Product> findAll();
    void create(ProductDto productDto);
    Product findById(int id);
    Boolean update(Product product);
    Boolean delete(int id);
    List<Product> listByCategory(int id);
}
