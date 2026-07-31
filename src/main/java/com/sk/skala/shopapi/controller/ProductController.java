package com.sk.skala.shopapi.controller;

import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.service.ProductService;
import com.sk.skala.shopapi.data.table.Product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Delegate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "상품 관리", description = "상품 CRUD 및 조회 API")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다. offset과 count를 통해 페이지네이션을 지원합니다.")
    public Response getAllProducts(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "10") int count) {
        return productService.getAllProducts(offset, count);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 통해 상품 상세 정보를 조회합니다.")
    public Response getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Response createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping
    public Response updateProduct(@Valid @RequestBody Product product) {    
        return productService.updateProduct(product);
    }

    @DeleteMapping
    public Response deleteProduct(@Valid @RequestBody Product product) {
        return productService.deleteProduct(product.getId());
    }
    
}
