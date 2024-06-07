package com.example.BuildPC.Service;

import com.example.BuildPC.models.Order;
import com.example.BuildPC.models.OrderDetail;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.models.ProductImage;
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
