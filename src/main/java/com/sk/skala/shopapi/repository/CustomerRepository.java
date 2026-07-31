package com.sk.skala.shopapi.repository;

import com.sk.skala.shopapi.data.table.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
