package com.example.BuildPC.controller;


import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Category;
import com.example.BuildPC.models.Product;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.repository.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ManagerDashBoard")
public class ProductController {

//    @Autowired
//    private ProductRepository  productRepository;
//    private CategoryRepository categoryRepository;
//
//
//    @GetMapping("/productList")
//    public String showProductList(Model model) {
//        List<Product> productList = productRepository.findAll();
//        model.addAttribute("products", productList);
//        return "/Manager/showProductList";
//    }
//
//    @GetMapping("/create")
//    public  String createProduct(Model model) {
//        ProductDto productDto = new ProductDto();
//        model.addAttribute("productDto", productDto);
//        return "Manager/createProduct";
//    }
//
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
//
//
//    }


}
