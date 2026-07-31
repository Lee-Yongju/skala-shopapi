package com.sk.skala.shopapi.repository;

import com.sk.skala.shopapi.data.table.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerProductRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCustomer_CustomerId(String customerId);
    OrderItem findByCustomerAndProduct(String customerId, Long productId);
}
