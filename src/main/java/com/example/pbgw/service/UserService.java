package com.example.pbgw.service;

import com.example.pbgw.dto.UserDto;
import com.example.pbgw.entity.User;

import java.util.List;


public interface UserService {
	void saveUser(UserDto userDto);

	User findUserByEmail(String email);
}
