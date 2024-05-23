package com.example.BuildPC.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "discount")
    private Float discount;

    @ManyToOne()
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne()
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;


}
