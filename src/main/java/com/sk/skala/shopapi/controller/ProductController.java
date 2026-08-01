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
    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다. offset과 count로 페이지네이션을, productName(부분 일치)·minPrice·maxPrice로 검색·가격 필터링을 지원합니다.")
    public Response getAllProducts(@RequestParam(defaultValue = "0") int offset,
                                    @RequestParam(defaultValue = "10") int count,
                                    @RequestParam(required = false) String productName,
                                    @RequestParam(required = false) Double minPrice,
                                    @RequestParam(required = false) Double maxPrice) {
        return productService.getAllProducts(offset, count, productName, minPrice, maxPrice);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 통해 상품 상세 정보를 조회합니다.")
    public Response getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @Operation(summary = "상품 등록", description = "상품명과 가격을 입력받아 신규 상품을 등록합니다.")
    public Response createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping
    @Operation(summary = "상품 정보 수정", description = "상품 ID에 해당하는 상품명과 가격을 수정합니다.")
    public Response updateProduct(@Valid @RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping
    @Operation(summary = "상품 삭제", description = "상품 ID에 해당하는 상품 정보를 삭제합니다.")
    public Response deleteProduct(@Valid @RequestBody Product product) {
        return productService.deleteProduct(product.getId());
    }
    
}
