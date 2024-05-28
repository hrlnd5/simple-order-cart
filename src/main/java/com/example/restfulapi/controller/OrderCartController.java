package com.example.restfulapi.controller;

import com.example.restfulapi.entity.User;
import com.example.restfulapi.model.WebResponse;
import com.example.restfulapi.model.order_cart.CreateOrderCartRequest;
import com.example.restfulapi.model.order_cart.OrderCartResponse;
import com.example.restfulapi.service.OrderCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/order-cart")
public class OrderCartController {

    @Autowired
    private OrderCartService orderCartService;

    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<OrderCartResponse> get(User user, @PathVariable("orderId") Long orderId) {
        OrderCartResponse response = orderCartService.get(user, orderId);

        return WebResponse.<OrderCartResponse>builder().data(response).build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderCartResponse> createOrderCart(
            User user,
            @RequestBody CreateOrderCartRequest request
    ) {
        OrderCartResponse response = orderCartService.createOrderCart(user, request);

        return WebResponse.<OrderCartResponse>builder().data(response).build();
    }

    @PostMapping(
            path = "/{cartId}/add-product",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderCartResponse> addProduct(
            User user,
            @PathVariable("cartId") Long cartId,
            @RequestParam(value = "productId", required = false) Long productId,
            @RequestParam(value = "quantity", required = false) Integer quantity
    ) {
        OrderCartResponse response = orderCartService.addProduct(user, cartId, productId, quantity);

        return WebResponse.<OrderCartResponse>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/{cartId}/delete-product",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderCartResponse> deleteProduct(
            User user,
            @PathVariable("cartId") Long cartId,
            @RequestParam("productId") Long productId
    ) {
        OrderCartResponse response = orderCartService.deleteProduct(user, cartId, productId);

        return WebResponse.<OrderCartResponse>builder().data(response).build();
    }

    @DeleteMapping(
            path = "/{cartId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteOrderCart(
            User user,
            @PathVariable("cartId") Long cartId
    ) {
        boolean isDeleted = orderCartService.delete(user, cartId);

        return isDeleted ? WebResponse.<String>builder().data("Order cart successfully deleted").build()
                : WebResponse.<String>builder().errors("Order cart delete failed").build();
    }

}
