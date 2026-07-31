package com.sk.skala.shopapi.service;

import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.repository.CustomerProductRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CustomerProductRepository customerProductRepository;
    // private final SessionHandler sessionHandler;
}
