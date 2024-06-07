package com.example.BuildPC.Service;

import com.example.BuildPC.model.*;
import com.example.BuildPC.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void postConstruct() {
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setRole(Role.ADMIN);
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("abc"));
            userRepository.save(adminUser);
        }
    }

    public void register(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {


            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            newUser.setEmail(user.getEmail());
            newUser.setRole(Role.CUSTOMER);
            newUser.setPosts(new HashSet<>());
            newUser.setComments(new HashSet<>());
            newUser.setOrders(new HashSet<>());

            userRepository.save(newUser);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
