package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.OrderDTO;
import com.example.BuildPC.models.Order;
import com.example.BuildPC.models.OrderDetail;
import com.example.BuildPC.repository.OrderDetailRepository;
import com.example.BuildPC.repository.OrderRepository;
import com.example.BuildPC.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    public List<OrderDTO> listAllOrder(){
        List<Order> orderList = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            orderDTOS.add(new OrderDTO(order,total));
        }
        return orderDTOS;
    }
    public void deleteOrderById(int id){
        orderRepository.deleteById(id);
    }
    public Order getOrderById(int id){
        return orderRepository.findById(id).get();
    }
    public float calculateTotal(Order order){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        float total = 0;

        for (OrderDetail orderDetail : orderDetails) {
            float productSalePrice = orderDetail.getProduct().getProductSalePrice();
            int quantity = orderDetail.getQuantity();
            float discount = orderDetail.getDiscount() != null ? orderDetail.getDiscount() : 1;

            total += quantity * productSalePrice * discount;
        }
        return total;
    }

}
