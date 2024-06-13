package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.ProductImageDto;
import com.example.BuildPC.models.ProductImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductImageService {
    List<ProductImage> findAllImage();

    void createProductImage(ProductImageDto productImage);
    void deleteProductImage(ProductImage productImage);
    void deleteAllProductImages(List<ProductImage> productImages);
    List<ProductImage> createAllProductImages(List<MultipartFile> images, Integer productId);
}
