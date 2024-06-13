package com.example.BuildPC.service;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<User> findAll();
    void save(UserDto userDto);
    User findUserById(int id);
    void upadteUser(User user);
    void deleteUserById(int id);
}
