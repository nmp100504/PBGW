package com.example.BuildPC.service;

import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements  AdminService{

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
        adminRepository.save(user);
    }

    @Override
    public void deleteUserById(long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public long countTotalAccounts() {
        return adminRepository.count();
    }

    @Override
    public long countActiveAccounts() {
        return adminRepository.countByIsEnabled(true);
    }

    @Override
    public long countInactiveAccounts() {
        return adminRepository.countByIsEnabled(false);
    }


    @Override
    public List<User> findActiveAccounts() {
        return adminRepository.findByIsEnabled(true);
    }

    @Override
    public List<User> findInactiveAccounts() {
        return adminRepository.findByIsEnabled(false);
    }

    @Override
    public void deactivateAccountById(long id) {
        User user = adminRepository.findById(id).get();
        if(user != null){
            user.setEnabled(false);
            updateUser(user);
        }
    }

    @Override
    public List<User> listByUserIsEnabled(boolean status) {
        return adminRepository.findByIsEnabled(status);
    }

    @Override
    public List<User> searchByUserEmailOrPhone(String emailOrPhone) {
        return adminRepository.searchByUserEmailOrPhone(emailOrPhone);
    }

    @Override
    public List<User> searchByUserEmailOrPhoneAndIsEnabled(String emailOrPhone, boolean status) {
        return adminRepository.findByUserEmailOrPhoneAndIsEnabled(emailOrPhone, status);
    }

    @Override
    public boolean existsByUserEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

}
