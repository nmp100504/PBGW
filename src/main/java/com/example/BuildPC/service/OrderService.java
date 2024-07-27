package com.example.BuildPC.service;

import com.example.BuildPC.dto.OrderDTO;
import com.example.BuildPC.model.Order;
import com.example.BuildPC.model.OrderDetail;
import com.example.BuildPC.model.Status;
import com.example.BuildPC.repository.OrderDetailRepository;
import com.example.BuildPC.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    public List<OrderDTO> listAllOrder(){
        List<Order> orderList = orderRepository.findAll();
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
            OrderDTO orderDTO = new OrderDTO(order, total);
            orderDTO.setOrderDetails(new HashSet<>(orderDetails));
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    public float calculateTotal(Order order){
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
        float total = 0;

        System.out.println("Order ID: " + order.getId() + ", Order Details Count: " + orderDetails.size());

        for (OrderDetail orderDetail : orderDetails) {
            float productSalePrice = orderDetail.getProduct().getProductSalePrice();
            int quantity = orderDetail.getQuantity();
            float discount = orderDetail.getDiscount() != null ? orderDetail.getDiscount() : 1;

            System.out.println("Product Sale Price: " + productSalePrice + ", Quantity: " + quantity + ", Discount: " + discount);

            total += quantity * productSalePrice * (1 - discount);
        }
        System.out.println("Total for Order ID " + order.getId() + ": " + total);
        return total;
    }
    public Map<String, Double> getTotalOrderValueByMonth() {
        List<OrderDTO> orders = listAllOrder();
        return orders.stream().collect(Collectors.groupingBy(
                order -> new SimpleDateFormat("yyyy-MM").format(order.getOrderDate()),
                Collectors.summingDouble(OrderDTO::getTotalPrice)
                        )
        );
    }
    public Map<String, Long> getOrderCountByMonth() {
        List<OrderDTO> orders = listAllOrder();
        return orders.stream().collect(Collectors.groupingBy(
                order -> new SimpleDateFormat("yyyy-MM").format(order.getOrderDate()),
                Collectors.counting()
        ));
    }
    public void deleteOrderById(int id){
        orderRepository.deleteById(id);
    }
    public Order getOrderById(int id){
        return orderRepository.findById(id).get();
    }
    public void saveOrder(Order order){
        orderRepository.save(order);
    }
    public List<OrderDTO> listOrdersByStatusAndDate(Status status,Date date ){
        List<Order> orderList = orderRepository.findByStatusAndOrderDate(status,date);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            orderDTOS.add(new OrderDTO(order,total));
        }
        return orderDTOS;

    }
    public List<OrderDTO> listByOrdersDate(Date date){
        List<Order> orderList = orderRepository.findByOrderDate(date);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            orderDTOS.add(new OrderDTO(order,total));
        }
        return orderDTOS;
    }
    public List<OrderDTO> listByOrderStatus(Status status){
        List<Order> orderList = orderRepository.findByStatus(status);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            orderDTOS.add(new OrderDTO(order,total));
        }
        return orderDTOS;
    }
    public List<OrderDTO> listByUserId(Integer id){
        List<Order> orderList = orderRepository.findByUserId(id);
        List<OrderDTO> orderDTOS = new ArrayList<>();

        for (Order order : orderList) {
            float total = calculateTotal(order);
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrder(order);
            OrderDTO orderDTO = new OrderDTO(order, total);
            orderDTO.setOrderDetails(new HashSet<>(orderDetails));
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }
    public int countOrdersByStatusAndDate(Status status, Date date) {
        return orderRepository.countByStatusAndOrderDate(status, date);
    }
    public int countOrderByStatus(Status status) {
        return orderRepository.countOrderByStatus(status);
    }
}
