package com.sk.skala.shopapi.service;

import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.tools.StringUtil;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Response<PagedList<Product>> getAllProducts(int offset, int count, String productName, Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new ParameterException("최소 가격은 최대 가격보다 클 수 없습니다.");
        }

        Pageable pageable = PageRequest.of(Math.max(0, offset), Math.max(1, count));
        Page<Product> productPage = productRepository.search(productName, minPrice, maxPrice, pageable);

        PagedList<Product> pagedList = new PagedList<>(
            productPage.getContent(),
            productPage.getNumber(),
            productPage.getSize(),
            productPage.getTotalElements(),
            productPage.getTotalPages()
        );

        return new Response<>(pagedList);
    }

    public Response<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .map(Response::new)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
    }

    public Response<Product> createProduct(Product product) {
        product.setId(null);
        validateProduct(product);

        if (productRepository.findByProductName(product.getProductName()).isPresent()) {
            throw new ResponseException(Error.DATA_DUPLICATED);
        }

        Product savedProduct = productRepository.save(product);
        return new Response<>(savedProduct);
    }

    public Response<Product> updateProduct(Product product) {
        if (product.getId() == null) {
            throw new ParameterException("수정할 상품의 id는 필수입니다.");
        }

        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        validateProduct(product);

        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductPrice(product.getProductPrice());

        Product savedProduct = productRepository.save(existingProduct);
        return new Response<>(savedProduct);
    }

    public Response<Product> deleteProduct(Long id) {
        if (id == null) {
            throw new ParameterException("삭제할 상품의 id는 필수입니다.");
        }

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        productRepository.delete(existingProduct);
        return new Response<>(existingProduct);
    }

    private void validateProduct(Product product) {
        if (StringUtil.isEmpty(product.getProductName()) || product.getProductPrice() == null || product.getProductPrice() <= 0) {
            throw new ParameterException("상품 이름과 가격을 올바르게 입력해주세요.");
        }
    }
}
