package com.example.BuildPC.controller.Manager;


import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.*;
import com.example.BuildPC.service.*;
import com.example.BuildPC.dto.ProductDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired CategoryService categoryService;

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/category/{id}")
    public String showCategory(@PathVariable("id") int id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Optional<User> user = userService.findByEmail(userDetails.getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                List<Product> listByCategory = productService.listByCategory(id);
                List<Category> categoryList = categoryService.findAll();
                model.addAttribute("listByCategory", listByCategory);
                model.addAttribute("categoryList", categoryList);
                String catename = categoryService.findCategoryById(id).getCategoryName();
                model.addAttribute("title", catename);
                return "LandingPage/shop_grid";
            }
        }
        return "auth/login_page";
    }

    @GetMapping("/product/{id}")
    public String showProductDetails(@PathVariable("id") int id, Model model) {
        Product byId = productService.findById(id);
        List<Category> categoryList = categoryService.findAll();
        List<Specifications> specList = specificationService.getAllSpecificationsByCategory(categoryService.findCategoryById(
                byId.getCategory().getId()));
        if(byId.getProductSpecifications().isEmpty()){
            System.out.println("No specifications found");
        }else {
            for(ProductSpecifications spec : byId.getProductSpecifications()){
                System.out.println(spec.getSpecValue());
            }
        }
        model.addAttribute("product", byId);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("specList", specList);
        return "LandingPage/shop_details";
    }
    @GetMapping("/ManagerDashBoard/productList")
    public String showProductList(Model model, @Param("productName") String productName ) {
        List<Product> productList = productService.findAll();
        if(productName != null && !productName.isEmpty()) {
            productList = productService.searchProductByName(productName);
            model.addAttribute("productName", productName);
        }
        model.addAttribute("products", productList);
        return "/Manager/showProductList";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("search") String query, Model model) {
        model.addAttribute("listByCategory", productService.findByProductNameContaining(query));
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("title", query);
        model.addAttribute("searchString", query); 
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
