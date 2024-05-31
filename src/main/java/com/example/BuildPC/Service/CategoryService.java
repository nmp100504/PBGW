package com.example.BuildPC.Service;


import com.example.BuildPC.dtos.CategoryDto;
import com.example.BuildPC.dtos.UserDto;
import com.example.BuildPC.models.Category;
import com.example.BuildPC.models.User;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    void save(CategoryDto categoryDto);
    Category findCategoryById(int id);
    void upadteCategory(Category category);
    void deleteCategoryById(int id);
}
