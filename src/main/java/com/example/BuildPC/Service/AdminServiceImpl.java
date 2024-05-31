package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.UserDto;
import com.example.BuildPC.models.Role;
import com.example.BuildPC.models.User;
import com.example.BuildPC.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements  AdminService{

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<User> findAll() {
        return adminRepository.findAll();
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User();
        user.setFistName(userDto.getFistName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setRole(Role.valueOf(userDto.getRole()));
        adminRepository.save(user);
    }

    @Override
    public User  findUserById(int id) {
        return adminRepository.findById(id).get();
    }

    @Override
    public void upadteUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setFistName(user.getFistName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setPhone(user.getPhone());
        userDto.setRole(String.valueOf(user.getRole()));
        adminRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        adminRepository.deleteById(id);
    }
}
