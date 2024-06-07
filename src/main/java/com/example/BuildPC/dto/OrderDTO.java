package com.example.BuildPC.dto;

import com.example.BuildPC.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private Date orderDate;
    private String status;
    private String shipAddress;
    private float totalPrice;

    public OrderDTO(Order order, float totalPrice) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.status = String.valueOf(order.getStatus());
        this.shipAddress = order.getShipAddress();
        this.totalPrice = totalPrice;
    }
}