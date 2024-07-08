package com.example.BuildPC.service;


import com.example.BuildPC.dto.CategoryDto;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    void save(CategoryDto categoryDto);
    Category findCategoryById(int id);
    void updateCategory(Category category);
    void deleteCategoryById(int id);
    void deActivateCategoryById(int id);
    List<Category> findCategoryByStatus();
    List<Category> searchCategoryByName(String keyword);
    boolean existCategoryByName(String categoryName);
    List<Category> listByCategoryStatus(boolean status);
    List<Category> searchByCategoryNameAndStatus(String categoryName, boolean status);


}
