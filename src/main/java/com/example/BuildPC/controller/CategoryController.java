package com.example.BuildPC.controller;



import com.example.BuildPC.Service.CategoryService;
import com.example.BuildPC.dtos.CategoryDto;
import com.example.BuildPC.models.Category;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ManagerDashBoard")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoryList")
    public String showCategoryList(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "Manager/showCategoryList";
    }

    @GetMapping("/categoryList/create")
    public String createCategory(Model model){
        CategoryDto categoryDto = new CategoryDto();
        model.addAttribute("categoryDto", categoryDto);
        return "Manager/createCategory";
    }

    @PostMapping("/categoryList/create")
    public String createCategory(@Valid @ModelAttribute("categoryDto") CategoryDto categoryDto, BindingResult result) {

        if(result.hasErrors()) {
            return "Manager/createCategory";
        }

        categoryService.save(categoryDto);
        return "redirect:/ManagerDashBoard/categoryList";
    }

    @GetMapping("/categoryList/delete")
    public String deleteCategory(@RequestParam("id") int id) {
        try{
            categoryService.deleteCategoryById(id);
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return "redirect:/ManagerDashBoard/categoryList";
        }
        return "redirect:/ManagerDashBoard/categoryList";
    }
}
