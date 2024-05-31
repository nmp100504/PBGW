package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.UserDto;
import com.example.BuildPC.models.User;
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
