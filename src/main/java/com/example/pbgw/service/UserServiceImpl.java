package com.example.pbgw.service;


import com.example.pbgw.dto.UserDto;
import com.example.pbgw.entity.Role;
import com.example.pbgw.entity.User;
import com.example.pbgw.repository.RoleRepository;
import com.example.pbgw.repository.UserRepository;
import com.example.pbgw.utils.TbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void saveUser(UserDto userDto) {
		Role role = roleRepository.findByName(TbConstants.Roles.USER);

		if (role == null)
			role = roleRepository.save(new Role(TbConstants.Roles.USER));

		User user = new User(userDto.getName(), userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()),
				Arrays.asList(role));
		userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}