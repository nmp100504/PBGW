package com.example.BuildPC.controller;


import com.example.BuildPC.Service.BrandService;
import com.example.BuildPC.Service.CategoryService;
import com.example.BuildPC.Service.ProductImageService;
import com.example.BuildPC.Service.ProductService;
import com.example.BuildPC.dtos.CategoryDto;
import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Brand;
import com.example.BuildPC.models.Category;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.models.ProductImage;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.repository.ProductImageRepository;
import com.example.BuildPC.repository.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/ManagerDashBoard")
public class ProductController {

    @Autowired
    private ProductService  productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductImageRepository productImageRepository;


    @GetMapping("/productList")
    public String showProductList(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("products", productList);
//        List<Category> categoryList = categoryService.findAll();
//        model.addAttribute("categories", categoryList);
//        List<Brand> brandList = brandService.findAll();
//        model.addAttribute("brands", brandList);

        return "/Manager/showProductList";
    }

    @GetMapping("/productList/create")
    public  String createProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        List<Category> categories = categoryService.findCategoryByStatus();
        model.addAttribute("categories", categories);
        List<Brand> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "Manager/createProduct";
    }

    @PostMapping("/productList/create")
    public String createProductManagerDashboard(Model model,@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result) {




        if(productDto.getProductImages().isEmpty()){
                result.addError(new FieldError("productDto", "productImages", "Product Images cannot be empty"));
        }
        if(productService.existsByProductName(productDto.getProductName())){
            result.addError(new FieldError("productDto", "productName", "Product Name already exists"));
        }
        if(result.hasErrors()) {
            List<Category> categories = categoryService.findCategoryByStatus();
            model.addAttribute("categories", categories);
            List<Brand> brands = brandService.findAll();
            model.addAttribute("brands", brands);
            return "Manager/createProduct";
        }
        productService.create(productDto);

        return "redirect:/ManagerDashBoard/productList";

    }

    @GetMapping("/productList/edit")
    public String showProductEdit(Model model, @RequestParam("id") int id) {
        try{
            Product product = productService.findProductById(id);
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setProductName(product.getProductName());
            productDto.setProductOriginalPrice(product.getProductOriginalPrice());
            productDto.setProductSalePrice(product.getProductSalePrice());
            productDto.setProductDesc(product.getProductDesc());
            productDto.setUnitsInStock(product.getUnitsInStock());
            productDto.setUnitsInOrder(product.getUnitsInOrder());
            productDto.setCategoryId(product.getCategory().getId());
            productDto.setBrandId(product.getBrand().getId());
            model.addAttribute("productDto", productDto);
            List<Category> categories = categoryService.findCategoryByStatus();
            model.addAttribute("categories", categories);
            List<Brand> brands = brandService.findAll();
            model.addAttribute("brands", brands);
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/productList";
        }
        return "Manager/editProduct";
    }
    @PostMapping("/productList/edit")
    public String showProductEdit(Model model,@RequestParam("id") int id,@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result) {

        try{

            Product product = productService.findProductById(id);
            model.addAttribute("productDto", productDto);
            if(result.hasErrors()) {
                List<Category> categories = categoryService.findCategoryByStatus();
                model.addAttribute("categories", categories);
                List<Brand> brands = brandService.findAll();
                model.addAttribute("brands", brands);
                return "Manager/editProduct";
            }


            Category category = categoryService.findCategoryById(productDto.getCategoryId());
            Brand brand = brandService.findByBranId(productDto.getBrandId());

            product.setProductName(productDto.getProductName());
            product.setProductOriginalPrice(productDto.getProductOriginalPrice());
            product.setProductSalePrice(productDto.getProductSalePrice());
            product.setProductDesc(productDto.getProductDesc());
            product.setUnitsInStock(productDto.getUnitsInStock());
            product.setUnitsInOrder(productDto.getUnitsInOrder());
            product.setCategory(category);
            product.setBrand(brand);
            product.setProductStatus(productDto.isProductStatus());
            productService.updateProdcut(product);
            // Cập nhật các ảnh hiện có và thêm các ảnh mới
            List<MultipartFile> images = productDto.getProductImages();
            if (images != null && !images.isEmpty() && images.stream().anyMatch(image -> !image.isEmpty())) {
                // Xóa các ảnh hiện có
                productImageService.deleteAllProductImages(product.getProductImages());

                // Lưu các ảnh mới
                productImageService.createAllProductImages(images, product.getId());
            }


        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/productList";
        }
        return "redirect:/ManagerDashBoard/productList";
    }



    @GetMapping("/productList/delete")
    public String deleteProduct(@RequestParam("id") int id) {
        try {
            productService.deleteProduct(id);
        }catch (Exception e) {
            System.out.println("Error in deleting product" + e.getMessage());
            return "redirect:/ManagerDashBoard/productList";
        }
        return "redirect:/ManagerDashBoard/productList";
    }

}
