package com.example.BuildPC.service;

import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public void create(ProductDto productDto) {
    }

    @Override
    public Product findById(int id) {
        return null;
    }

    @Override
    public Boolean update(Product product) {
        return null;
    }

    @Override
    public Boolean delete(int id) {
        return null;
    }

    @Override
    public List<Product> listByCategory(int id){
        return productRepository.findByCategoryId(id);
    }
}
