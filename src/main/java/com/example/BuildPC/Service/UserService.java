package com.example.BuildPC.Service;

import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private static List<User> users = new ArrayList<>();

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void postConstruct() {
        User user = new User();
        user.setRole(Role.CUSTOMER);
        user.setEmail("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        users.add(user);
    }

    public void register(User user) {
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        users.add(user);
    }

    public User findByLogin(String login) {
        return users.stream().filter(user -> user.getEmail().equals(login))
                .findFirst()
                .orElse(null);
    }
}
