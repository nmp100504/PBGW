package com.example.pbgw.service;


import com.example.pbgw.dto.UserRegistrationDto;
import com.example.pbgw.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}
