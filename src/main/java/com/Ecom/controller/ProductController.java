package com.Ecom.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.dto.AddProductRequest;
import com.Ecom.model.Product;
import com.Ecom.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ==================== ADD PRODUCT ====================
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @RequestPart("data") String rawData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        // Convert JSON String → AddProductRequest
        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(rawData, AddProductRequest.class);

        Product savedProduct = productService.addProduct(request, imageFile);
        return ResponseEntity.ok(savedProduct);
    }

    // ==================== UPDATE PRODUCT ====================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("data") String rawData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        // Convert JSON string into the Java object
        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(rawData, AddProductRequest.class);

        Product updatedProduct = productService.updateProduct(id, request, imageFile);
        return ResponseEntity.ok(updatedProduct);
    }

    // ==================== GET ALL PRODUCTS ====================
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ==================== GET ONE PRODUCT ====================
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ==================== FILTER BY CATEGORY ====================
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    // ==================== SEARCH ====================
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // ==================== GET IMAGE ENDPOINT ====================
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        byte[] imageBytes = productService.getProductImage(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageBytes);
    }
    

    // GET similar products
    @GetMapping("/{id}/similar")
    public List<Product> getSimilarProducts(@PathVariable Long id) {
        return productService.getSimilarProducts(id);
    }

    // GET product suggestions
//    @GetMapping("/{id}/suggestions")
//    public List<Product> getSuggestions(@PathVariable Long id) {
//        return productService.getSuggestedProducts(id);
//    }
}
