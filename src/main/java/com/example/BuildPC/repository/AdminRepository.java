package com.example.BuildPC.repository;

import com.example.BuildPC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {

    long countByIsEnabled(boolean status);
    List<User> findByIsEnabled(boolean status);
    @Query("SELECT u FROM User u WHERE u.email  LIKE %?1% OR u.phone LIKE %?1%")
    List<User> searchByUserEmailOrPhone(String emailOrPhone);

    @Query("SELECT u FROM User u WHERE (u.email LIKE %?1% OR u.phone LIKE %?1%) AND u.isEnabled = ?2")
    List<User> findByUserEmailOrPhoneAndIsEnabled(String emailOrPhone, boolean status);
}
