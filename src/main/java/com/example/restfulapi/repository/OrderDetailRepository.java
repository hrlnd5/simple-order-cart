package com.example.restfulapi.repository;

import com.example.restfulapi.entity.OrderCart;
import com.example.restfulapi.entity.OrderDetail;
import com.example.restfulapi.entity.OrderDetailId;
import com.example.restfulapi.entity.Product;
import com.example.restfulapi.model.order_detail.OrderDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {

    @Query(
            "SELECT new com.example.restfulapi.model.order_detail.OrderDetailResponse" +
                    "(p.name, pt.name, p.price, od.quantity, od.total) " +
                    "FROM OrderDetail od " +
                    "JOIN od.orderCart oc " +
                    "JOIN od.product p " +
                    "JOIN p.productTypeId pt " +
                    "WHERE oc.orderCartId = :orderCartId"
    )
    List<OrderDetailResponse> getOrderDetails(@Param("orderCartId") Long orderCartId);

    @Query(value = "SELECT SUM(od.total) " +
            "FROM OrderDetail od " +
            "WHERE od.orderCart = :orderCart")
    double getSubtotalByOrderCart(@Param("orderCart") OrderCart orderCart);

    List<OrderDetail> findByOrderCart(OrderCart orderCart);

    boolean existsByProduct(Product product);
}
