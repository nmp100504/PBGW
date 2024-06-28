package com.example.BuildPC.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    public OrderDetail(Integer quantity, Float discount, Order order, Product product) {
        this.quantity = quantity;
        this.discount = discount;
        this.order = order;
        this.product = product;
    }
}
