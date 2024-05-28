package com.example.restfulapi.model.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {

    @NotNull(message = "required")
    private Long productId;

    @Size(max = 255, message = "maximum 255 character")
    private String name;

    @Max(value = 2147483647, message = "maximum value 2147483648")
    private int stock;

    private float price;

    private Long productTypeId;
}
