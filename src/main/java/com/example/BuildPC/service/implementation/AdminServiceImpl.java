package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.AdminRepository;
import com.example.BuildPC.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;


    @Override
    public List<User> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setEnabled(userDto.isEnabled());
        adminRepository.save(user);
    }

    @Override
    public User  findUserById(long id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public void updateUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(passwordEncoder.encode(user.getPassword()));
        userDto.setPhone(user.getPhone());
        userDto.setRole(String.valueOf(user.getRole()));
        userDto.setEnabled(user.isEnabled());
        adminRepository.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        adminRepository.deleteById(id);
    }

//    @Override
//    public List<User> findByUserStatus() {
//        return adminRepository.findByUserStatus(true);
//    }
}
