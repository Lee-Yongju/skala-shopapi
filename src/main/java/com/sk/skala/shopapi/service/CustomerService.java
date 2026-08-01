package com.sk.skala.shopapi.service;

import com.sk.skala.shopapi.repository.CustomerRepository;
import com.sk.skala.shopapi.repository.ProductRepository;
import com.sk.skala.shopapi.repository.CustomerProductRepository;

import com.sk.skala.shopapi.common.PagedList;
import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.common.SessionHandler;
import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.data.dto.OrderItemDto;
import com.sk.skala.shopapi.data.dto.OrderListDto;
import com.sk.skala.shopapi.data.dto.OrderRequest;
import com.sk.skala.shopapi.data.table.Customer;
import com.sk.skala.shopapi.data.table.OrderItem;
import com.sk.skala.shopapi.data.table.Product;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.exception.ParameterException;
import com.sk.skala.shopapi.exception.ResponseException;
import com.sk.skala.shopapi.tools.StringUtil;

import jakarta.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final CustomerProductRepository customerProductRepository;
    private final SessionHandler sessionHandler;

    public Response<PagedList<Customer>> getAllCustomers(int offset, int count) {
        Pageable pageable = PageRequest.of(Math.max(0, offset), Math.max(1, count));
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        PagedList<Customer> pagedList = new PagedList<>(
            customerPage.getContent(),
            customerPage.getNumber(),
            customerPage.getSize(),
            customerPage.getTotalElements(),
            customerPage.getTotalPages()
        );

        return new Response<>(pagedList);
    }

    @Transactional(readOnly = true)
    public Response<OrderListDto> getCustomerById(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        List<OrderItemDto> products = customerProductRepository.findByCustomer_CustomerId(customerId).stream()
                .map(item -> OrderItemDto.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getProductName())
                        .productPrice(item.getProduct().getProductPrice())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        OrderListDto orderListDto = OrderListDto.builder()
                .customerId(customer.getCustomerId())
                .customerPoint(customer.getCustomerPoint())
                .products(products)
                .build();

        return new Response<>(orderListDto);
    }

    public Response<Customer> createCustomer(Customer customer) {
        if (StringUtil.isAnyEmpty(customer.getCustomerId(), customer.getCustomerPassword())) {
            throw new ParameterException("고객 ID와 비밀번호를 올바르게 입력해주세요.");
        }

        if (customerRepository.findById(customer.getCustomerId()).isPresent()) {
            throw new ResponseException(Error.DATA_DUPLICATED);
        }

        if (customer.getCustomerPoint() == null) {
            customer.setCustomerPoint(0.0);
        } else if (customer.getCustomerPoint() < 0) {
            throw new ParameterException("초기 포인트는 0 이상이어야 합니다.");
        }

        Customer savedCustomer = customerRepository.save(customer);
        return new Response<>(savedCustomer);
    }

    public Response<Customer> loginCustomer(CustomerSession loginRequest, HttpSession session) {
        if (StringUtil.isAnyEmpty(loginRequest.getCustomerId(), loginRequest.getCustomerPassword())) {
            throw new ParameterException("고객 ID와 비밀번호를 올바르게 입력해주세요.");
        }

        Customer customer = customerRepository.findById(loginRequest.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        if (!customer.getCustomerPassword().equals(loginRequest.getCustomerPassword())) {
            throw new ResponseException(Error.UNAUTHORIZED);
        }

        sessionHandler.login(session, customer.getCustomerId());
        return new Response<>(customer);
    }

    public Response<Customer> updateCustomer(String customerId, Customer customer) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        if (!StringUtil.isEmpty(customer.getCustomerPassword())) {
            existingCustomer.setCustomerPassword(customer.getCustomerPassword());
        }
        if (customer.getCustomerPoint() != null) {
            if (customer.getCustomerPoint() < 0) {
                throw new ParameterException("보유 포인트는 0 이상이어야 합니다.");
            }
            existingCustomer.setCustomerPoint(customer.getCustomerPoint());
        }

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return new Response<>(savedCustomer);
    }

    @Transactional
    public Response<Customer> deleteCustomer(String customerId) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        customerProductRepository.deleteAll(customerProductRepository.findByCustomer_CustomerId(customerId));
        customerRepository.delete(existingCustomer);
        return new Response<>(existingCustomer);
    }

    @Transactional
    public Response<OrderItem> placeOrder(OrderRequest order, HttpSession session) {
        CustomerSession customerSession = sessionHandler.getSession(session);

        Customer customer = customerRepository.findById(customerSession.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        double totalPrice = product.getProductPrice() * order.getQuantity();
        if (customer.getCustomerPoint() < totalPrice) {
            throw new ResponseException(Error.INSUFFICIENT_FUNDS);
        }
        customer.setCustomerPoint(customer.getCustomerPoint() - totalPrice);
        customerRepository.save(customer);

        OrderItem orderItem = customerProductRepository.findByCustomerAndProduct(customer, product)
                .map(item -> {
                    item.setQuantity(item.getQuantity() + order.getQuantity());
                    return item;
                })
                .orElseGet(() -> OrderItem.builder()
                        .customer(customer)
                        .product(product)
                        .quantity(order.getQuantity())
                        .build());

        OrderItem savedOrderItem = customerProductRepository.save(orderItem);
        return new Response<>(savedOrderItem);
    }

    @Transactional
    public Response<String> cancelOrder(OrderRequest order, HttpSession session) {
        CustomerSession customerSession = sessionHandler.getSession(session);

        Customer customer = customerRepository.findById(customerSession.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));
        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        OrderItem orderItem = customerProductRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new ResponseException(Error.DATA_NOT_FOUND));

        if (order.getQuantity() > orderItem.getQuantity()) {
            throw new ResponseException(Error.INSUFFICIENT_QUANTITY);
        }

        if (order.getQuantity().equals(orderItem.getQuantity())) {
            customerProductRepository.delete(orderItem);
        } else {
            orderItem.setQuantity(orderItem.getQuantity() - order.getQuantity());
            customerProductRepository.save(orderItem);
        }

        double refundAmount = product.getProductPrice() * order.getQuantity();
        customer.setCustomerPoint(customer.getCustomerPoint() + refundAmount);
        customerRepository.save(customer);

        return new Response<>(Error.SUCCESS.getMessage());
    }
}
