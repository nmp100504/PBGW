package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.ProductImageDto;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.ProductImage;
import com.example.BuildPC.repository.ProductImageRepository;
import com.example.BuildPC.repository.ProductRepository;
import com.example.BuildPC.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductImage> findAllImage() {
        return productImageRepository.findAll();
    }

    @Override
    public List<ProductImage> findByProduct(Product product) {
        return List.of();
    }

    @Override
    public void createProductImage(ProductImageDto productImageDto) {
       try{
           Optional<Product> product = productRepository.findById(productImageDto.getProductId());
           if(product.isEmpty()){
               System.out.println("Product not found");
               return;
           }
           ProductImage productImage = new ProductImage();
           productImage.setImageFileName(String.valueOf(productImageDto.getImageFileName()));
           productImage.setProduct(product.get());
           productImageRepository.save(productImage);
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }

    @Override
    public void deleteProductImage(ProductImage productImage) {
            productImageRepository.delete(productImage);
    }

    @Override
    public void deleteAllProductImages(List<ProductImage> productImages) {
        productImageRepository.deleteAll(productImages);
    }

    @Override
    public List<ProductImage> createAllProductImages(List<MultipartFile> images, Integer productId) {
        List<ProductImage> productImages = new ArrayList<>();
        String uploadDir ="public/images/Product/";
        Path uploadPath = Paths.get(uploadDir);
        try{
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            for(MultipartFile image : images){
                if(!image.isEmpty()){
                    String storeFileName = image.getOriginalFilename();
                    try (InputStream inputStream = image.getInputStream()){
                        Files.copy(inputStream, Paths.get(uploadDir + storeFileName),
                                StandardCopyOption.REPLACE_EXISTING);
                        ProductImage productImage = new ProductImage();
                        productImage.setImageFileName(storeFileName);

                        //Tìm sản phẩm bằng ID
                        Product product = productRepository.findById(productId).orElse(null);
                        if(product != null){
                            productImage.setProduct(product);
                            productImageRepository.save(productImage);
                            productImages.add(productImage);
                        }
                    }catch (Exception ex) {
                        System.out.println("Exception: " + ex.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productImages;
    }


}
