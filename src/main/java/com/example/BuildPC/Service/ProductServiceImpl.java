package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Brand;
import com.example.BuildPC.models.Category;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.repository.BrandRepository;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
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
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setProductSlug(productDto.getProductSlug());
            product.setCategory(category.get());
            product.setProductOriginalPrice(productDto.getProductOriginalPrice());
            product.setProductSalePrice(productDto.getProductSalePrice());
            product.setProductDesc(productDto.getProductDesc());
            product.setUnitsInStock(productDto.getUnitsInStock());
            product.setUnitsInOrder(productDto.getUnitsInOrder());
            product.setBrand(brand.get());

            productRepository.save(product);
        }catch (Exception e){
            System.out.println("Error while creating product" + e.getMessage());
        }

    }

    @Override
    public Product findProductById(int id) {
        return productRepository.findById(id).get();
    }

    @Override
    public void updateProdcut(Product product) {
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
