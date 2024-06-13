package com.example.BuildPC.service;

import com.example.BuildPC.model.Order;
import com.example.BuildPC.model.OrderDetail;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.ProductImage;
import com.example.BuildPC.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired private ProductImageRepository productImageRepository;

    public List<ProductImage> findByProduct(Product product) {

        return productImageRepository.findByProduct(product);
    }
}
