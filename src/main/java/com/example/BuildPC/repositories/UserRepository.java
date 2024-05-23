package com.example.BuildPC.repositories;

import com.example.BuildPC.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
