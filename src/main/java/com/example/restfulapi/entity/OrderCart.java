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
@Table(name = "order_carts")
public class OrderCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderCartId;

    @Column(nullable = false, length = 50)
    private String customerName;

    @Column(columnDefinition = "TEXT")
    private String customerAddress;

    private double total;

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

    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp orderedAt;
}
