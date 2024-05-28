package com.example.restfulapi.model.order_cart;

import com.example.restfulapi.model.order_detail.OrderDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCartResponse {

    private Long orderId;

    private String customerName;

    private String customerAddress;

    private double total;

    private List<OrderDetailResponse> details;
}
