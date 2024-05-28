package com.example.BuildPC.Service;

import com.example.BuildPC.models.Order;
import com.example.BuildPC.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;

    public List<Order> listAllOrder(){
        return  (List<Order>)  orderRepository.findAll();
    }
    public Order getOrderById(int id){
        return orderRepository.findById(id).get();
    }
    public void removeOrderById(int id){
        orderRepository.deleteById(id);
    }

}
