package com.sk.skala.shopapi.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {
    private String customerId;
    private Double customerPoint;
    private List<OrderItemDto> products;
}
