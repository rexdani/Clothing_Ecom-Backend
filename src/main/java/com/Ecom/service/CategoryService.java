package com.Ecom.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.dto.CategoryDTO;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Category;
import com.Ecom.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CategoryDTO dto, MultipartFile imageFile) throws IOException {

        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImage(imageFile.getBytes());
        }

        return categoryRepository.save(category);
    }

    // UPDATE CATEGORY (multipart)
    public Category updateCategory(Long id, CategoryDTO dto, MultipartFile imageFile) throws IOException {

        Category category = getCategoryById(id);

        category.setName(dto.getName());
        category.setDescription(dto.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImage(imageFile.getBytes());
        }

        return categoryRepository.save(category);
    }

    // LIST ALL
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // GET ONE
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    // DELETE
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
