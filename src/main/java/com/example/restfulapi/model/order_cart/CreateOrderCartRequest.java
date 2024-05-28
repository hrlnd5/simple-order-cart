package com.example.restfulapi.model.order_cart;

import com.example.restfulapi.model.order_detail.OrderDetailRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderCartRequest {

    @NotNull(message = "required")
    private String customerName;

    @NotNull(message = "required")
    private String customerAddress;

    @NotNull(message = "required")
    private List<OrderDetailRequest> detailRequests;

}
