package com.example.BuildPC.repository;

import com.example.BuildPC.model.Order;
import com.example.BuildPC.model.Status;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    // Custom query to find orders by status and order date (ignoring time)
    @Query("SELECT o FROM Order o WHERE o.status = :status AND FUNCTION('DATE', o.orderDate) = :orderDate")
    List<Order> findByStatusAndOrderDate(@Param("status") Status status, @Param("orderDate") LocalDate orderDate);

    // Custom query to count orders by status and order date (ignoring time)
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status AND FUNCTION('DATE', o.orderDate) = :orderDate")
    int countByStatusAndOrderDate(@Param("status") Status status, @Param("orderDate") LocalDate orderDate);

    // Custom query to find orders by order date (ignoring time)
    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.orderDate) = :orderDate")
    List<Order> findByOrderDate(@Param("orderDate") LocalDate orderDate);

    // Method to find orders by status
    List<Order> findByStatus(Status status);

    // Custom query to count orders by status
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    int countOrderByStatus(@Param("status") Status status);

    // Method to find orders by user ID
    List<Order> findByUserId(Integer userID);

}
