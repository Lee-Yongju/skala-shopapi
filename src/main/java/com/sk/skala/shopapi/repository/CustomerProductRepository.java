package com.sk.skala.shopapi.repository;

import com.sk.skala.shopapi.data.table.Customer;
import com.sk.skala.shopapi.data.table.OrderItem;
import com.sk.skala.shopapi.data.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerProductRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCustomer_CustomerId(String customerId);
    Optional<OrderItem> findByCustomerAndProduct(Customer customer, Product product);
}
