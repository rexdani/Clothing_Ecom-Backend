package com.Ecom.service;

import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Category;
import com.Ecom.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Create
    public Category createCategory(Category category) {

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        if (category.getImage() != null) {
            try {
                byte[] imgBytes = Base64.getDecoder().decode(category.getImage());
                category.setImage(imgBytes);
            } catch (Exception e) {
                throw new RuntimeException("Invalid Base64 image format");
            }
        }

        return categoryRepository.save(category);
    }

    // Get all
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get by ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    // Update
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = getCategoryById(id);

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        if (categoryDetails.getImage() != null) {
            try {
                byte[] imgBytes = Base64.getDecoder().decode(categoryDetails.getImage());
                category.setImage(imgBytes);
            } catch (Exception e) {
                throw new RuntimeException("Invalid Base64 image format");
            }
        }

        return categoryRepository.save(category);
    }

    // Delete
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
