package com.Ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.Ecom.model.Category;
import com.Ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	// Find all products by category
	List<Product> findByCategory(Category category);

	// Optional: search products by name containing keyword
	List<Product> findByNameContainingIgnoreCase(String keyword);
	
	// Get similar products (same category, exclude the current one)
	@Query("SELECT p FROM Product p WHERE p.category = :category AND p.id <> :productId")
	List<Product> findSimilarProducts(Category category, Long productId);
}
