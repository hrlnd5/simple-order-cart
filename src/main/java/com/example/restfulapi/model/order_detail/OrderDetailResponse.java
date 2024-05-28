package com.example.restfulapi.model.order_detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {

    private String product;

    private String type;

    private double price;

    private int quantity;

    private double total;
}
