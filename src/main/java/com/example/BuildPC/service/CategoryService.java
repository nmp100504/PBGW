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
    List<Category> findActiveCategories();
    List<Category> searchCategoryByName(String keyword);
    boolean existCategoryByName(String categoryName);
    List<Category> listByCategoryStatus(boolean status);
    List<Category> searchByCategoryNameAndStatus(String categoryName, boolean status);
    long countTotalCategories();
    long countActiveCategories();
    long countInActiveCategories();

    List<Category> findInActiveCategories();


}
