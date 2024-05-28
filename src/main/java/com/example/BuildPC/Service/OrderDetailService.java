package com.example.BuildPC.Service;

import com.example.BuildPC.models.Order;
import com.example.BuildPC.models.OrderDetail;
import com.example.BuildPC.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

       public List<OrderDetail> getAllOrderDetails() {
           return orderDetailRepository.findAll();
       }
        public void deleteByOrder(Order order) {
            List<OrderDetail> byOrder = orderDetailRepository.findByOrder(order);
            orderDetailRepository.deleteAll(byOrder);
        }
}