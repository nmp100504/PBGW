package com.example.BuildPC.service;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminService {
    List<User> findAll();
    void save(UserDto userDto);
    User findUserById(long id);
    void updateUser(User user);
    void deleteUserById(long id);
    long countTotalAccounts();
    long countActiveAccounts();
    long countInactiveAccounts();
    List<User> findActiveAccounts();
    List<User> findInactiveAccounts();
    void deactivateAccountById(long id);
    List<User> listByUserIsEnabled(boolean status);
    List<User> searchByUserEmailOrPhone(String emailOrPhone);
    List<User> searchByUserEmailOrPhoneAndIsEnabled(String emailOrPhone, boolean status);
    boolean existsByUserEmail(String email);
}
