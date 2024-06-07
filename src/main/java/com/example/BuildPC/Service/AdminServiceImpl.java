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
        user.setFirstName(userDto.getFirstName());
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
        adminRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        adminRepository.deleteById(id);
    }
}
