package com.example.restfulapi.service;

import com.example.restfulapi.entity.*;
import com.example.restfulapi.model.order_cart.CreateOrderCartRequest;
import com.example.restfulapi.model.order_cart.OrderCartResponse;
import com.example.restfulapi.model.order_detail.OrderDetailResponse;
import com.example.restfulapi.repository.OrderCartRepository;
import com.example.restfulapi.repository.OrderDetailRepository;
import com.example.restfulapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderCartService {

    @Autowired
    private OrderCartRepository orderCartRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValidationService validationService;

    private OrderCartResponse toOrderCartResponse(OrderCart orderCart, List<OrderDetailResponse> orderDetails) {
        return OrderCartResponse
                .builder()
                .orderId(orderCart.getOrderCartId())
                .customerName(orderCart.getCustomerName())
                .customerAddress(orderCart.getCustomerAddress())
                .total(orderCart.getTotal())
                .details(orderDetails)
                .build();
    }

    @Transactional(readOnly = true)
    public OrderCartResponse get(User user, Long orderId) {
        OrderCart orderCart = orderCartRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cart is not found"));

        List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(orderId);

        return toOrderCartResponse(orderCart, orderDetails);
    }

    @Transactional
    public OrderCartResponse addProduct(User user, Long cartId, Long productId, Integer quantity) {
        if (cartId == null || cartId < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cartId : is required");

        if (productId == null || productId < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "productId : is required");

        if (quantity == null || quantity < 1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum 1 quantity");

        OrderCart orderCart = orderCartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Cart is not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

        if (quantity > product.getStock())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock is not enough");

        double total = product.getPrice() * quantity;

        OrderDetailId orderDetailId = new OrderDetailId();
        orderDetailId.setOrderCartId(cartId);
        orderDetailId.setProductId(productId);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(orderDetailId);
        orderDetail.setOrderCart(orderCart);
        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setTotal(total);
        orderDetailRepository.save(orderDetail);

        orderCart.setTotal(orderDetailRepository.getSubtotalByOrderCart(orderCart));
        orderCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderCart.setUpdatedBy(user);
        orderCartRepository.save(orderCart);

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(cartId);
        return toOrderCartResponse(orderCart, orderDetails);
    }

    @Transactional
    public OrderCartResponse deleteProduct(User user, Long cartId, Long productId) {
        OrderCart orderCart = orderCartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Cart is not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

        List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(cartId);

        if (orderDetails.size() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product cannot be deleted, you can delete order cart");
        }

        OrderDetailId orderDetailId = new OrderDetailId();
        orderDetailId.setOrderCartId(cartId);
        orderDetailId.setProductId(productId);
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order detail is not found"));
        orderDetailRepository.delete(orderDetail);

        orderCart.setTotal(orderDetailRepository.getSubtotalByOrderCart(orderCart));
        orderCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderCart.setUpdatedBy(user);
        orderCartRepository.save(orderCart);

        product.setStock(product.getStock() + orderDetail.getQuantity());
        productRepository.save(product);

        orderDetails = orderDetailRepository.getOrderDetails(cartId);

        return toOrderCartResponse(orderCart, orderDetails);
    }

    @Transactional
    public OrderCartResponse createOrderCart(User user, CreateOrderCartRequest request) {
        validationService.validate(request);

        OrderCart orderCart = new OrderCart();
        orderCart.setCustomerName(request.getCustomerName());
        orderCart.setCustomerAddress(request.getCustomerAddress());
        orderCart.setCreatedBy(user);
        orderCartRepository.save(orderCart);
        double subTotal = 0;

        for (int i = 0; i < request.getDetailRequests().size(); i++) {
            Long productId = request.getDetailRequests().get(i).getProductId();
            int quantity = request.getDetailRequests().get(i).getQuantity();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

            if (quantity > product.getStock())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock with productId " + productId + " is not enough");

            double total = product.getPrice() * quantity;
            subTotal += total;

            OrderDetailId orderDetailId = new OrderDetailId();
            orderDetailId.setOrderCartId(orderCart.getOrderCartId());
            orderDetailId.setProductId(productId);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(orderDetailId);
            orderDetail.setOrderCart(orderCart);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setTotal(total);
            orderDetailRepository.save(orderDetail);

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        orderCart.setTotal(subTotal);
        orderCartRepository.save(orderCart);

        List<OrderDetailResponse> orderDetails = orderDetailRepository.getOrderDetails(orderCart.getOrderCartId());

        return toOrderCartResponse(orderCart, orderDetails);
    }

    @Transactional
    public boolean delete(User user, Long cartId) {
        OrderCart orderCart = orderCartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order Cart is not found"));

        List<OrderDetail> listOrderDetail = orderDetailRepository.findByOrderCart(orderCart);

        for (int i = 0; i < listOrderDetail.size(); i++) {
            Long productId = listOrderDetail.get(i).getProduct().getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product is not found"));

            OrderDetailId orderDetailId = new OrderDetailId();
            orderDetailId.setOrderCartId(cartId);
            orderDetailId.setProductId(productId);
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order detail is not found"));
            orderDetailRepository.delete(orderDetail);

            product.setStock(product.getStock() + orderDetail.getQuantity());
            productRepository.save(product);
        }

        orderCartRepository.delete(orderCart);

        return true;
    }
}
