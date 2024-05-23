package com.example.BuildPC.repositories;

import com.example.BuildPC.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
