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
public class CreateProductRequest {

    @NotBlank(message = "required")
    @Size(max = 255, message = "maximum 255 character")
    private String name;

    @NotNull(message = "required")
    @Min(value = 1, message = "minimum value 1")
    @Max(value = 2147483647, message = "maximum value 2147483648")
    private int stock;

    @NotNull(message = "required")
    @Min(value = 1, message = "minimum value 1")
    private float price;

    @NotNull(message = "required")
    private Long productTypeId;
}
