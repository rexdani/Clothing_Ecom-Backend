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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Ecom.dto.AddProductRequest;
import com.Ecom.dto.ProductDTO;
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
    public ResponseEntity<ProductDTO> addProduct(
            @RequestPart("data") String rawData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(rawData, AddProductRequest.class);

        Product savedProduct = productService.addProduct(request, imageFile);
        return ResponseEntity.ok(convertProductToDTO(savedProduct));
    }

    // ==================== UPDATE PRODUCT ====================
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart("data") String rawData,
            @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        AddProductRequest request = mapper.readValue(rawData, AddProductRequest.class);

        Product updatedProduct = productService.updateProduct(id, request, imageFile);
        return ResponseEntity.ok(convertProductToDTO(updatedProduct));
    }

    // ==================== GET ALL PRODUCTS ====================
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtoList = productService.getAllProducts()
                .stream()
                .map(this::convertProductToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // ==================== GET ONE PRODUCT ====================
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(convertProductToDTO(product));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ==================== FILTER BY CATEGORY ====================
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDTO> dtoList = productService.getProductsByCategory(categoryId)
                .stream()
                .map(this::convertProductToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // ==================== SEARCH ====================
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> dtoList = productService.searchProducts(keyword)
                .stream()
                .map(this::convertProductToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
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

    // ==================== SIMILAR PRODUCTS ====================
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<ProductDTO>> getSimilarProducts(@PathVariable Long id) {

        List<ProductDTO> dtoList = productService.getSimilarProducts(id)
                .stream()
                .map(this::convertProductToDTO)
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    // ==================== CONVERTER ====================
    private ProductDTO convertProductToDTO(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setCategoryId(product.getCategory().getId());

        if (product.getImage() != null) {
            dto.setImageBase64(Base64.getEncoder().encodeToString(product.getImage()));
        }

        return dto;
    }

}
