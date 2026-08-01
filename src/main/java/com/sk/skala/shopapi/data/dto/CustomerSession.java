package com.sk.skala.shopapi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSession {
    private String customerId;
    private String customerPassword;

    public CustomerSession(String customerId) {
        this.customerId = customerId;
    }
}
