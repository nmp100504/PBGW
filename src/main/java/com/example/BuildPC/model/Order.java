package com.example.BuildPC.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;


    @Column(name = "order_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @Column(name = "order_note")
    private String orderNote;

    @Column(name = "ship_address")
    private String shipAddress;

    @Column(name =  "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    public Order(Date orderDate, String orderNote, String shipAddress, Status status, User user) {
        this.orderDate = orderDate;
        this.orderNote = orderNote;
        this.shipAddress = shipAddress;
        this.status = status;
        this.user = user;
    }
    public Order(Date orderDate, String orderNote, String shipAddress, User user) {
        this.orderDate = orderDate;
        this.orderNote = orderNote;
        this.shipAddress = shipAddress;
        this.status = Status.WAIT;
        this.user = user;
    }

}
