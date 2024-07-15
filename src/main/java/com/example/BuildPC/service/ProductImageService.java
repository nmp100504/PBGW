package com.example.BuildPC.service;

import com.example.BuildPC.dto.ProductImageDto;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.ProductImage;
import com.example.BuildPC.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductImageService {
    List<ProductImage> findAllImage();

    List<ProductImage> findByProduct(Product product);
    void createProductImage(ProductImageDto productImage);
    void deleteProductImage(ProductImage productImage);
    void deleteAllProductImages(List<ProductImage> productImages);
    List<ProductImage> createAllProductImages(List<MultipartFile> images, Integer productId);

}
