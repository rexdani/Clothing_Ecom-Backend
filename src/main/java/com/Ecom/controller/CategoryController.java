package com.Ecom.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.dto.CategoryDTO;
import com.Ecom.model.Category;
import com.Ecom.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

	 @Autowired
	    private CategoryService categoryService;


	    // ===================== CREATE CATEGORY =====================
	    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<CategoryDTO> createCategory(
	            @RequestPart("data") String rawData,
	            @RequestPart(value = "image", required = false) MultipartFile imageFile
	    ) throws IOException {

	        ObjectMapper mapper = new ObjectMapper();
	        CategoryDTO dto = mapper.readValue(rawData, CategoryDTO.class);

	        Category saved = categoryService.createCategory(dto, imageFile);
	        return ResponseEntity.ok(convertToDTO(saved));
	    }


	    // ===================== UPDATE CATEGORY =====================
	    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<CategoryDTO> updateCategory(
	            @PathVariable Long id,
	            @RequestPart("data") String rawData,
	            @RequestPart(value = "image", required = false) MultipartFile imageFile
	    ) throws IOException {
	    	

	        ObjectMapper mapper = new ObjectMapper();
	        CategoryDTO dto = mapper.readValue(rawData, CategoryDTO.class);

	        Category updated = categoryService.updateCategory(id, dto, imageFile);
	        return ResponseEntity.ok(convertToDTO(updated));
	    }


	    // ===================== GET ALL =====================
	    @GetMapping
	    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
	        List<CategoryDTO> list = categoryService.getAllCategories()
	                .stream()
	                .map(this::convertToDTO)
	                .toList();

	        return ResponseEntity.ok(list);
	    }


	    // ===================== GET ONE =====================
	    @GetMapping("/{id}")
	    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
	        Category category = categoryService.getCategoryById(id);
	        return ResponseEntity.ok(convertToDTO(category));
	    }


	    // ===================== DELETE =====================
	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
	        categoryService.deleteCategory(id);
	        return ResponseEntity.ok("Category deleted successfully");
	    }


	    // ===================== CONVERTER =====================
	    private CategoryDTO convertToDTO(Category category) {
	        CategoryDTO dto = new CategoryDTO();

	        dto.setId(category.getId());
	        dto.setName(category.getName());
	        dto.setDescription(category.getDescription());

	        if (category.getImage() != null) {
	            dto.setImageBase64(Base64.getEncoder().encodeToString(category.getImage()));
	        }

	        return dto;
	    }
}
