package com.sk.skala.shopapi.service;

import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.exception.Error;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Response getAllProducts(int offset, int count) {
        Pageable pageable = PageRequest.of(Math.max(0, offset), Math.max(1, count));
        Page<Product> productPage = productRepository.findAll(pageable);

        PagedList<Product> pagedList = new PagedList<>(
            productPage.getContent(),
            productPage.getNumber(),
            productPage.getSize(),
            productPage.getTotalElements(),
            productPage.getTotalPages()
        );

        return new Response(pagedList);
    }

    public Response getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> new Response(product))
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
    }

    public Response createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        if (savedProduct.getProductName() == null || savedProduct.getProductPrice() <= 0) {
            throw new ParameterException(Error.DATA_DUPLICATE);
        }


        return new Response(savedProduct);
    }


}
