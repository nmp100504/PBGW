package com.example.BuildPC.controller;


import com.example.BuildPC.service.BrandService;
import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.service.ProductImageService;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.Brand;
import com.example.BuildPC.model.ProductImage;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository  productRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired CategoryService categoryService;

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductImageRepository productImageRepository;
    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable("id") int id, Model model) {
        List<Product> listByCategory = productService.listByCategory(id);
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("listByCategory", listByCategory);
        model.addAttribute("categoryList", categoryList);
        String catename = categoryService.findCategoryById(id).getCategoryName();
        model.addAttribute("title", catename);
        return "LandingPage/shop_grid";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model) {
        Product byId = productService.findById(id);
        List<Category> categoryList = categoryService.findAll();
        if(byId ==null ){
            System.out.println("No product found");
        }
        model.addAttribute("product", byId);
        model.addAttribute("categoryList", categoryList);
        return "LandingPage/shop_details";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("search") String query, Model model) {
        model.addAttribute("listByCategory", productService.findByProductNameContaining(query));
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("title", query);
        model.addAttribute("categoryList", categoryList);
        return "LandingPage/shop_grid";
    }

    @GetMapping("/sort/{id}")
    public String sort(@RequestParam("sortingOption") int sortingOption,@PathVariable("id") int id, Model model) {
        List<Product> products = productService.listByCategory(id);

        if(sortingOption == 1){
            products.sort(Comparator.comparing(Product::getProductSalePrice));
            model.addAttribute("listByCategory", products);
        }
        if(sortingOption == 2){
            Collections.sort(products, Comparator.comparing(Product::getProductSalePrice).reversed());
            model.addAttribute("listByCategory", products);
        }
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "LandingPage/shop_grid";
    }

    @GetMapping("/filterProductsByPrice")
    @ResponseBody
    public String filterProductsByPrice(@RequestParam int minPrice, @RequestParam int maxPrice, Model model) {
        List<Product> filteredProducts = productService.findByProductSalePriceBetween(minPrice,maxPrice);
        model.addAttribute("listByCategory", filteredProducts);
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "LandingPage/shop_grid"; // Return the HTML fragment with updated products
    }
//    @GetMapping("/productList")
//    public String showProductList(Model model) {
//        List<Product> productList = productService.findAll();
//        model.addAttribute("products", productList);
//        return "/Manager/showProductList";
//    }
//
//    @GetMapping("/ManagerDashBoard/create")
//    public  String createProduct(Model model) {
//        ProductDto productDto = new ProductDto();
//        model.addAttribute("productDto", productDto);
//        return "Manager/createProduct";
//    }

//    @PostMapping("/create")
//    public String createProductManagerDashboard(@RequestParam int id, @Valid @ModelAttribute ProductDto productDto, BindingResult result) {
//
//        if(result.hasErrors()) {
//            return "Manager/createProduct";
//        }
//        Category category = categoryRepository.findById(id)
//        Product product = new Product();
//        product.setProductName(productDto.getProductName());
//        product.setProductOriginalPrice(productDto.getProductOriginalPrice());
//        product.setProductSalePrice(productDto.getProductSalePrice());
//        product.setProductDesc(productDto.getProductDesc());
//        product.setUnitsInStock(productDto.getUnitsInStock());
//        product.setUnitsInOrder(productDto.getUnitsInOrder());
//
//    }




    @GetMapping("/ManagerDashBoard/productList/create")
    public  String createProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        List<Category> categories = categoryService.findCategoryByStatus();
        model.addAttribute("categories", categories);
        List<Brand> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "Manager/createProduct";
    }

    @PostMapping("/ManagerDashBoard/productList/create")
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

    @GetMapping("/ManagerDashBoard/productList/edit")
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
    @PostMapping("/ManagerDashBoard/productList/edit")
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
            productService.updateProduct(product);
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



    @GetMapping("/ManagerDashBoard/productList/delete")
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
