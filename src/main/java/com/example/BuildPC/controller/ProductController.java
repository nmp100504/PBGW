package com.example.BuildPC.controller;


import com.example.BuildPC.Service.BrandService;
import com.example.BuildPC.Service.CategoryService;
import com.example.BuildPC.Service.ProductService;
import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Brand;
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

    @Autowired
    private ProductService  productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


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
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        List<Brand> brands = brandService.findAll();
        model.addAttribute("brands", brands);
        return "Manager/createProduct";
    }

    @PostMapping("/productList/create")
    public String createProductManagerDashboard(@Valid @ModelAttribute("productDto") ProductDto productDto, BindingResult result) {

        if(result.hasErrors()) {
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
            model.addAttribute("productDto", productDto);
            List<Category> categories = categoryService.findAll();
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
                return "Manager/editProduct";
            }
            Category category = categoryService.findCategoryById(id);
            Brand brand = brandService.findByBranId(id);

            product.setProductName(productDto.getProductName());
            product.setProductOriginalPrice(productDto.getProductOriginalPrice());
            product.setProductSalePrice(productDto.getProductSalePrice());
            product.setProductDesc(productDto.getProductDesc());
            product.setUnitsInStock(productDto.getUnitsInStock());
            product.setUnitsInOrder(productDto.getUnitsInOrder());
            product.setCategory(category);
            product.setBrand(brand);
            productService.updateProdcut(product);

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
