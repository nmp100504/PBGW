package com.example.BuildPC.repository;

import com.example.BuildPC.model.PasswordResetToken;
import com.example.BuildPC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByUser(User user);
    void deleteByUser(User user);
    Optional<PasswordResetToken> findByToken(String theToken);
}
