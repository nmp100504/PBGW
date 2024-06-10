package com.example.BuildPC.Service;

import com.example.BuildPC.model.User;
import com.example.BuildPC.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForUser(User user, String token);
    Optional<VerificationToken> findByToken(String token);


    void deleteUserToken(Long id);
}
