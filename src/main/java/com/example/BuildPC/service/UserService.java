package com.example.BuildPC.service;

import com.example.BuildPC.dto.RegistrationRequest;
import com.example.BuildPC.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User registerUser(RegistrationRequest registrationRequest);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

//    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);

    public void updateUser(User user, String password);

    public void updateUser(User user, MultipartFile avatar);
}
