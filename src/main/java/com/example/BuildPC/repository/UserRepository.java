package com.example.BuildPC.repository;


import com.example.BuildPC.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}