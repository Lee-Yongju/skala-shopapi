package com.sk.skala.shopapi.controller;

import com.sk.skala.shopapi.common.Response;
import com.sk.skala.shopapi.service.CustomerService;
import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.data.dto.OrderRequest;
import com.sk.skala.shopapi.data.table.Customer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "고객 관리", description = "고객 CRUD, 로그인, 주문/취소 API")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "고객 목록 조회", description = "고객 목록을 조회합니다. offset과 count를 통해 페이지네이션을 지원합니다.")
    public Response getAllCustomers(@RequestParam(defaultValue = "0") int offset,
                                     @RequestParam(defaultValue = "10") int count) {
        return customerService.getAllCustomers(offset, count);
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "고객 상세 조회", description = "고객 ID로 고객 정보와 주문한 상품 목록을 조회합니다.")
    public Response getCustomerById(@PathVariable String customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping
    @Operation(summary = "고객 등록", description = "고객 ID와 비밀번호로 신규 고객을 등록합니다.")
    public Response createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PostMapping("/login")
    @Operation(summary = "고객 로그인", description = "customerId와 customerPassword로 로그인하고 세션을 발급합니다.")
    public Response loginCustomer(@RequestBody CustomerSession customerSession, HttpSession session) {
        return customerService.loginCustomer(customerSession, session);
    }

    @PutMapping("/{customerId}")
    @Operation(summary = "고객 정보 수정", description = "고객 ID에 해당하는 비밀번호 또는 보유 포인트를 수정합니다.")
    public Response updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        return customerService.updateCustomer(customerId, customer);
    }

    @DeleteMapping("/{customerId}")
    @Operation(summary = "고객 삭제", description = "고객 ID에 해당하는 고객 정보를 삭제합니다.")
    public Response deleteCustomer(@PathVariable String customerId) {
        return customerService.deleteCustomer(customerId);
    }

    @PostMapping("/order")
    @Operation(summary = "상품 주문", description = "로그인한 고객이 상품을 주문합니다. 보유 포인트에서 주문 금액만큼 차감됩니다.")
    public Response placeOrder(@Valid @RequestBody OrderRequest order, HttpSession session) {
        return customerService.placeOrder(order, session);
    }

    @PostMapping("/cancel")
    @Operation(summary = "주문 취소", description = "로그인한 고객이 주문한 상품을 취소합니다. 취소 금액만큼 포인트가 환급됩니다.")
    public Response cancelOrder(@Valid @RequestBody OrderRequest order, HttpSession session) {
        return customerService.cancelOrder(order, session);
    }
}
