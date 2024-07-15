package com.example.BuildPC.service.implementation;


import com.example.BuildPC.dto.CategoryDto;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.repository.CategoryRepository;
import com.example.BuildPC.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void save(CategoryDto categoryDto) {
        //save image file
        MultipartFile image = categoryDto.getCategoryImage();
        String storeFileName = image.getOriginalFilename();

        try {
            String uploadDir = "public/images/Category/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storeFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }


        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategorySlug(categoryDto.getCategorySlug());
        category.setCategoryDesc(categoryDto.getCategoryDesc());
        category.setCategoryImage(storeFileName);
        category.setCategoryStatus(categoryDto.isCategoryStatus());

        categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deActivateCategoryById(int id) {
        Category category = categoryRepository.findById(id).get();
        if(category != null){
            category.setCategoryStatus(false);
            updateCategory(category);
        }
    }

    @Override
    public List<Category> findActiveCategories() {
        return categoryRepository.findByCategoryStatus(true);
    }
    @Override
    public List<Category> findInActiveCategories() {
        return categoryRepository.findByCategoryStatus(false);
    }
    @Override
    public List<Category> searchCategoryByName(String keyword) {
        return this.categoryRepository.searchCategoryName(keyword);
    }

    @Override
    public boolean existCategoryByName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    @Override
    public List<Category> listByCategoryStatus(boolean status) {
        return categoryRepository.findByCategoryStatus(status);
    }

    @Override
    public List<Category> searchByCategoryNameAndStatus(String categoryName, boolean status) {
        return categoryRepository.searchByCategoryNameAndStatus(categoryName,status);
    }

    @Override
    public long countTotalCategories() {
        return categoryRepository.count();
    }

    @Override
    public long countActiveCategories() {
        return categoryRepository.countByCategoryStatus(true);
    }

    @Override
    public long countInActiveCategories() {
        return categoryRepository.countByCategoryStatus(false);
    }


}
