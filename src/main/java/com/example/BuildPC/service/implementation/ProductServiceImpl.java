package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.Brand;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.repository.BrandRepository;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.repository.ProductRepository;
import com.example.BuildPC.service.ProductImageService;
import com.example.BuildPC.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductImageService productImageService;

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public void create(ProductDto productDto) {

        try{
            Optional<Category> category = categoryRepository.findById(productDto.getCategoryId());
            if(category.isEmpty()) {
                System.out.println("Category not found");
                return;
            }
            Optional<Brand> brand = brandRepository.findById(productDto.getBrandId());
            if(brand.isEmpty()) {
                System.out.println("Brand not found");
                return;
            }
//            if(productRepository.existsByProductName(productDto.getProductName())){
//                System.out.println("Product name already exists");
//                return;
//            }


            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setProductSlug(productDto.getProductSlug());
            product.setCategory(category.get());
            product.setProductOriginalPrice(productDto.getProductOriginalPrice());
            product.setProductSalePrice(productDto.getProductSalePrice());
            product.setProductDesc(productDto.getProductDesc());
            product.setUnitsInStock(productDto.getUnitsInStock());
            product.setUnitsInOrder(productDto.getUnitsInOrder());
            product.setProductStatus(productDto.isProductStatus());
            product.setBrand(brand.get());

            Product saveProduct =  productRepository.save(product);

            List<MultipartFile> images = productDto.getProductImages();
            productImageService.createAllProductImages(images, saveProduct.getId());
        }catch (Exception e){
            System.out.println("Error while creating product" + e.getMessage());
        }
    }

    @Override
    public Boolean update(Product product) {
        return null;
    }

    @Override
    public Boolean delete(int id) {
        return null;
    }

    public Product findById(int id) {
        return productRepository.getReferenceById(id);
    }

    @Override
    public Product findProductById(int id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void updateProduct(Product product) {
        if(productRepository.existsById(product.getId())){
            productRepository.save(product);
        }else {
            throw new RuntimeException("Product with id " + product.getId() + " does not exist");
        }
    }

    @Override
    public void deleteProduct(int id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product != null) {
            productImageService.deleteAllProductImages(product.getProductImages());
            productRepository.deleteById(id);
        }
    }

    @Override
    public List<Product> listByCategory(int id){
        return productRepository.findByCategoryId(id);
    }

    @Override
    public List<Product> listActiveProduct(boolean status) {
        return productRepository.findByProductStatus(status);
    }



    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByProductName(productName);
    }

    @Override
    public List<Product> findByProductNameContaining(String productName) {
        return productRepository.findByProductNameContaining(productName);
    }

    @Override
    public List<Product> findByProductSalePriceBetween(int minPrice, int maxPrice) {
        return productRepository.findByProductSalePriceBetween(minPrice,maxPrice);
    }

    @Override
    public List<Product> findByOrderByProductSalePriceAsc() {
        return productRepository.findByOrderByProductSalePriceAsc();
    }

    @Override
    public List<Product> findByOrderByProductSalePriceDesc() {
        return productRepository.findByOrderByProductSalePriceDesc();
    }

    @Override
    public List<Product> searchProductByName(String productName) {
        return this.productRepository.searchProductName(productName);
    }

//    @Override
//    public List<Product> findByProductStatus() {
//        return productRepository.findByProductStatus(true);
//    }
}
