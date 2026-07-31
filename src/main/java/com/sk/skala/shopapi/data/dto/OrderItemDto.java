package com.sk.skala.shopapi.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;

    @NotBlank(message = "상품 이름은 필수입니다")
    private String productName;

    @NotNull(message = "상품 가격은 필수입니다")
    private Double productPrice;

    @NotNull(message = "상품 수량은 필수입니다")
    private Integer quantity;
}
