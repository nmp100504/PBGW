package com.example.BuildPC.repository;

import com.example.BuildPC.model.Order;
import com.example.BuildPC.model.Status;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByStatusAndOrderDate(Status status, Date orderDate);
    int countByStatusAndOrderDate(Status status, Date orderDate);
    public List<Order> findByOrderDate(Date orderDate);
    public List<Order> findByStatus(Status status);
    public int countOrderByStatus(Status status);
    public List<Order> findByUserId(Integer userID);

}
