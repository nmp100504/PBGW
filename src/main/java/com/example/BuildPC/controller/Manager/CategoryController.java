package com.example.BuildPC.controller.Manager;



import com.example.BuildPC.model.Product;
import com.example.BuildPC.service.CategoryService;
import com.example.BuildPC.dto.CategoryDto;
import com.example.BuildPC.model.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/categoryList")
    public String showCategoryList(Model model, @Param("categoryName") String categoryName, @RequestParam(required = false) String status) {
        List<Category> categories = categoryService.findAll();
        if(categoryName != null){
            categories = categoryService.searchCategoryByName(categoryName);
            model.addAttribute("categoryName", categoryName);
        }
        if(status != null && !status.isEmpty()){
            boolean isActive = status.equalsIgnoreCase("active");
            categories = categoryService.listByCategoryStatus(isActive);
            model.addAttribute("status", status);
        }
        if(categoryName != null && !categoryName.isEmpty() && status != null && !status.isEmpty()){
            boolean isActive = status.equalsIgnoreCase("active");
            categories = categoryService.searchByCategoryNameAndStatus(categoryName,isActive);
        }
        long totalCategories = categoryService.countTotalCategories();
        model.addAttribute("totalCategories", totalCategories);
        long activeCategories = categoryService.countActiveCategories();
        model.addAttribute("activeCategories", activeCategories);
        long inActiveCategories = categoryService.countInActiveCategories();
        model.addAttribute("inActiveCategories", inActiveCategories);
        model.addAttribute("categories", categories);
        return "Manager/showCategoryList";
    }

    //Thống kê hiển thị ra danh sách tài khoản có trạng thái
    @GetMapping("/categoryList/activeCategory")
    public String showProductListActive(Model model) {
        List<Category> categoryList = categoryService.findActiveCategories();
        model.addAttribute("categories", categoryList);
        long totalCategories = categoryService.countTotalCategories();
        model.addAttribute("totalCategories", totalCategories);
        long activeCategories = categoryService.countActiveCategories();
        model.addAttribute("activeCategories", activeCategories);
        long inActiveCategories = categoryService.countInActiveCategories();
        model.addAttribute("inActiveCategories", inActiveCategories);
        return "/Manager/showCategoryList";
    }
    @GetMapping("/categoryList/inActiveCategory")
    public String showProductListInActive(Model model) {
        List<Category> categoryList = categoryService.findInActiveCategories();
        model.addAttribute("categories", categoryList);
        long totalCategories = categoryService.countTotalCategories();
        model.addAttribute("totalCategories", totalCategories);
        long activeCategories = categoryService.countActiveCategories();
        model.addAttribute("activeCategories", activeCategories);
        long inActiveCategories = categoryService.countInActiveCategories();
        model.addAttribute("inActiveCategories", inActiveCategories);
        return "/Manager/showCategoryList";
    }

    @GetMapping("/categoryList/create")
    public String createCategory(Model model){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryStatus(true);
        model.addAttribute("categoryDto", categoryDto);
        return "Manager/createCategory";
    }

    @PostMapping("/categoryList/create")
    public String createCategory(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult result) {
        if(categoryDto.getCategoryImage().isEmpty()){
            result.addError(new FieldError("categoryDto", "categoryImage", "Please select a file"));
        }
        if(categoryService.existCategoryByName(categoryDto.getCategoryName())){
            result.addError(new FieldError("categoryDto", "categoryName", "Category already exists"));
        }
        if(result.hasErrors()) {
            return "Manager/createCategory";
        }

        categoryService.save(categoryDto);
        return "redirect:/ManagerDashBoard/categoryList";
    }

    @GetMapping("/categoryList/edit")
    public String updateCategory(Model model, @RequestParam int id){
        try{
            Category category = categoryService.findCategoryById(id);
            model.addAttribute("category", category);


            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDto.setCategoryDesc(category.getCategoryDesc());

            categoryDto.setCategoryStatus(category.isCategoryStatus());
            model.addAttribute("categoryDto", categoryDto);
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/categoryList";
        }

        return "Manager/editCategory";
    }
    @PostMapping("/categoryList/edit")
    public String updateCategory(Model model, @RequestParam int id , @Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult result) {
        try{
            Category category = categoryService.findCategoryById(id);
            model.addAttribute("category", category);
            if(result.hasErrors()) {
                return "Manager/editCategory";
            }
            if(!categoryDto.getCategoryImage().isEmpty()){
                //Xoa anh cu
                String uploadDir = "public/images/Category/";
                Path oldImagePath =  Paths.get(uploadDir + category.getCategoryImage());

                try{
                    Files.delete(oldImagePath);
                }catch (Exception ex){
                    System.out.println("Exception: " + ex.getMessage());
                }

                //Luu anh moi
                MultipartFile multipartFile = categoryDto.getCategoryImage();
                String storageFileName = multipartFile.getOriginalFilename();
                try(InputStream inputStream = multipartFile.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }
                category.setCategoryImage(storageFileName);

                category.setCategoryName(categoryDto.getCategoryName());
                category.setCategoryDesc(categoryDto.getCategoryDesc());
                category.setCategoryStatus(categoryDto.isCategoryStatus());
                categoryService.updateCategory(category);
            }else{
                category.setCategoryName(categoryDto.getCategoryName());
                category.setCategoryDesc(categoryDto.getCategoryDesc());
                category.setCategoryStatus(categoryDto.isCategoryStatus());
                categoryService.updateCategory(category);
            }
        }catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/categoryList";
        }
        return "redirect:/ManagerDashBoard/categoryList";
    }

    @GetMapping("/categoryList/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        try{
            //categoryService.deleteCategoryById(id);
            categoryService.deActivateCategoryById(id);
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/categoryList";
        }
        return "redirect:/ManagerDashBoard/categoryList";
    }
}
