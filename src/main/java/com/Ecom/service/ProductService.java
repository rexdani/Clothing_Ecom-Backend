package com.Ecom.service;

import com.Ecom.dto.AddProductRequest;
import com.Ecom.exception.ResourceNotFoundException;
import com.Ecom.model.Category;
import com.Ecom.model.Product;
import com.Ecom.repository.CategoryRepository;
import com.Ecom.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // =============== ADD PRODUCT WITH IMAGE ===============
    public Product addProduct(AddProductRequest request, MultipartFile imageFile) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        byte[] imageBytes = null;
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                imageBytes = imageFile.getBytes();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process product image", e);
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .image(imageBytes) // <---- NEW: store longblob
                .imageUrl("/products/image/temp") // optional (can be dynamic later)
                .build();

        return productRepository.save(product);
    }

    // =============== UPDATE PRODUCT WITH IMAGE ===============
    public Product updateProduct(Long productId, AddProductRequest request, MultipartFile imageFile) {

        Product product = getProductById(productId);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                product.setImage(imageFile.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to update product image", e);
        }

        return productRepository.save(product);
    }

    // =============== GET ALL PRODUCTS ===============
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // =============== GET PRODUCT BY ID ===============
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id: " + productId)
                );
    }

    // =============== DELETE PRODUCT ===============
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    // =============== GET PRODUCTS BY CATEGORY ===============
    public List<Product> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return productRepository.findByCategory(category);
    }

    // =============== SEARCH PRODUCTS ===============
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // =============== GET PRODUCT IMAGE BLOB ===============
    public byte[] getProductImage(Long id) {
        Product product = getProductById(id);
        return product.getImage();   // returning byte[]
    }

    // Get similar products (same category)
    public List<Product> getSimilarProducts(Long productId) {
        Product product = getProductById(productId);
        Category category = product.getCategory();

        return productRepository.findSimilarProducts(category, productId);
    }

//    // Suggestions (smart suggestions)
//    public List<Product> getSuggestedProducts(Long productId) {
//        Product product = getProductById(productId);
//        Category category = product.getCategory();
//
//        List<Product> suggestions = productRepository.findTopRatedByCategory(category);
//
//        // If suggestion list is small, fill with global top rated
//        if (suggestions.size() < 5) {
//            suggestions.addAll(productRepository.findGlobalTopRated());
//        }
//
//        // Limit to 10 results max
//        return suggestions.stream().limit(10).toList();
//    }
}
