package com.example.BuildPC.service.implementation;

import com.example.BuildPC.model.PasswordResetToken;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.PasswordResetTokenRepository;
import com.example.BuildPC.repository.UserRepository;
import com.example.BuildPC.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static com.example.BuildPC.utils.TokenExpirationTime.getExpirationTime;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(theToken);
        if (passwordResetToken.isEmpty()){
            return "invalid";
        }
        Calendar calendar = Calendar.getInstance();
        if ((passwordResetToken.get().getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUser(user);
        PasswordResetToken resetToken;
        if (existingToken.isPresent()) {
            resetToken = existingToken.get();
            resetToken.setToken(passwordResetToken);
            resetToken.setExpirationTime(getExpirationTime());
        } else {
            resetToken = new PasswordResetToken(passwordResetToken, user);
        }
        passwordResetTokenRepository.save(resetToken);
    }
}