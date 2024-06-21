package com.example.BuildPC.repository;

import com.example.BuildPC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    //List<User> findByUserStatus(boolean status);
}
