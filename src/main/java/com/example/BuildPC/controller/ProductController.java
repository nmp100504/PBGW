package com.example.BuildPC.controller;


import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.service.ProductService;
import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository  productRepository;
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired CategoryService categoryService;

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

    @GetMapping("/ManagerDashBoard/productList")
    public String showProductList(Model model) {
        List<Product> productList = productRepository.findAll();
        model.addAttribute("products", productList);
        return "/Manager/showProductList";
    }

    @GetMapping("/ManagerDashBoard/create")
    public  String createProduct(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "Manager/createProduct";
    }

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


}
