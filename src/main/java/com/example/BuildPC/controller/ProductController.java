package com.example.BuildPC.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
