package com.example.restfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id", nullable = false)
    private ProductType productTypeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "DECIMAL(10.2)")
    private double price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "userId", nullable = false)
    private User createdBy;

    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "updated_by", referencedColumnName = "userId")
    private User updatedBy;
}
