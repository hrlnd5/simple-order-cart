package com.example.restfulapi.controller;

import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.PagingResponse;
import com.example.restfulapi.model.WebResponse;
import com.example.restfulapi.model.product.CreateProductRequest;
import com.example.restfulapi.model.product.ProductResponse;
import com.example.restfulapi.model.product.SearchProductRequest;
import com.example.restfulapi.model.product.UpdateProductRequest;
import com.example.restfulapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> get(User user, @PathVariable("productId") Long productId) {
        ProductResponse response = productService.get(user, productId);
        return WebResponse.<ProductResponse>builder().data(response).build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> create(User user, @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(user, request);
        return WebResponse.<ProductResponse>builder().data(response).build();
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(User user, @RequestBody UpdateProductRequest request) {
        ProductResponse response = productService.update(user, request);
        return WebResponse.<ProductResponse>builder().data(response).build();
    }

    @DeleteMapping(path = "{productId}")
    public WebResponse<String> delete(User user, @PathVariable("productId") Long productId) {
        boolean isDeleted = productService.delete(user, productId);
        return isDeleted ? WebResponse.<String>builder().data("Product successfully deleted").build() :
                WebResponse.<String>builder().errors("Product failed to delete").build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ProductResponse>> search(
            User user,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrize", required = false) String minPrize,
            @RequestParam(value = "maxPrize", required = false) String maxPrize,
            @RequestParam(value = "productType", required = false) String productType,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        SearchProductRequest request = SearchProductRequest
                .builder()
                .name(name)
                .minPrice(minPrize)
                .maxPrice(maxPrize)
                .productType(productType)
                .page(page)
                .size(size)
                .build();

        Page<ProductResponse> responses = productService.search(user, request);
        return WebResponse.<List<ProductResponse>>builder()
                .data(responses.getContent())
                .paging(
                        PagingResponse.builder()
                                .currentPage(responses.getNumber())
                                .totalPage(responses.getTotalPages())
                                .size(responses.getSize())
                                .build()
                )
                .build();
    }
}
