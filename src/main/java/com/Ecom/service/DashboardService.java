package com.Ecom.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Ecom.dto.DashboardStatsDTO;
import com.Ecom.repository.OrderRepository;
import com.Ecom.repository.ProductRepository;
import com.Ecom.repository.UserRepository;

@Service
public class DashboardService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	public DashboardStatsDTO getDashboardStats() {
		DashboardStatsDTO stats = new DashboardStatsDTO();

		// Get total users count
		long totalUsers = userRepository.count();
		stats.setTotalUsers(totalUsers);

		// Get total products count
		long totalProducts = productRepository.count();
		stats.setTotalProducts(totalProducts);

		// Get total orders count
		long totalOrders = orderRepository.count();
		stats.setTotalOrders(totalOrders);

		// Calculate total revenue from all orders
		BigDecimal totalRevenue = orderRepository.sumTotalAmount();
		if (totalRevenue == null) {
			totalRevenue = BigDecimal.ZERO;
		}
		stats.setTotalRevenue(totalRevenue);

		return stats;
	}
}
