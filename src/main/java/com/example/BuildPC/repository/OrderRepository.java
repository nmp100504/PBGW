package com.example.BuildPC.repository;

import com.example.BuildPC.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
