package com.example.restfulapi.model.order_detail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailRequest {

    @NotNull(message = "required")
    @Min(value = 1, message = "minimum value 1")
    private Long productId;

    private int quantity;

}
