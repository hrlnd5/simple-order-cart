package com.example.restfulapi.model.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchProductRequest {

    private String name;

    private String minPrice;

    private String maxPrice;

    private String productType;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;
}
