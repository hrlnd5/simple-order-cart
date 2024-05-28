package com.example.restfulapi.repository;

import com.example.restfulapi.entity.OrderCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCartRepository extends JpaRepository<OrderCart, Long> {
}
