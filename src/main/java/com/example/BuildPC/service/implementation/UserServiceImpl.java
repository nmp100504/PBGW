package com.example.BuildPC.service.implementation;

import com.example.BuildPC.dto.RegistrationRequest;
import com.example.BuildPC.mapper.PostMapper;
import com.example.BuildPC.model.Post;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.UserRepository;
import com.example.BuildPC.service.UserService;
import com.example.BuildPC.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;

    // Method to get the current logged-in user
    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        return findByEmail(email);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest registration) {
        var user = new User
                (registration.getEmail(),
                registration.getFirstName(),
                registration.getLastName(),
                passwordEncoder.encode(registration.getPassword()),
                registration.getPhone(),
                Role.CUSTOMER);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    @Transactional
    @Override
    public void deleteUser(Long id) {
        Optional<User> theUser = userRepository.findById(id);
        theUser.ifPresent(user -> verificationTokenService.deleteUserToken(user.getId()));
        userRepository.deleteById(id);
    }
    @Override
    public void updateUser(User user, String password) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    @Override
    public void updateUser(User user, MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            try {
                user.setAvatar(avatar.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to save thumbnail image", e);
            }
        }
        userRepository.save(user);
    }
}
