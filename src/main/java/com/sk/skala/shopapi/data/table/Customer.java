package com.sk.skala.shopapi.data.table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    
    @Id
    private String customerId;

    @Column(nullable = false)
    private String customerPassword;

    @Column
    private Double customerPoint;

    @Builder
    public Customer(String customerId, Double custPoint) {
        this.customerId = customerId;
        this.customerPoint = custPoint;
    }
}
