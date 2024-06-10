package com.example.BuildPC.Service;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<User> findAll();
    void save(UserDto userDto);
    User findUserById(long id);
    void updateUser(User user);
    void deleteUserById(long id);
}
