package com.example.BuildPC.dtos;

import com.example.BuildPC.models.Order;
import com.example.BuildPC.models.OrderDetail;
import com.example.BuildPC.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private Date orderDate;
    private String status;
    private String shipAddress;
    private User user;
    private Set<OrderDetail> orderDetails;
    private float totalPrice;

    public OrderDTO(Order order, float totalPrice) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.status = String.valueOf(order.getStatus());
        this.shipAddress = order.getShipAddress();
        this.orderDetails = order.getOrderDetails();
        this.user = order.getUser();
        this.totalPrice = totalPrice;
    }
}