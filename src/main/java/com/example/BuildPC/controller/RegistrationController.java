package com.example.BuildPC.controller;

import com.example.BuildPC.service.*;
import com.example.BuildPC.dto.RegistrationRequest;
import com.example.BuildPC.model.RegistrationCompleteEvent;
import com.example.BuildPC.model.User;
import com.example.BuildPC.model.VerificationToken;
import com.example.BuildPC.utils.UrlUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenServiceImpl tokenService;
    private final PasswordResetTokenService  passwordResetTokenService;
    private final RegistrationCompleteEventListener eventListener;


    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "auth/registration_page";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registration, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = userService.registerUser(registration);
        publisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/registration/registration-form";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                redirectAttributes.addFlashAttribute("message", "Token expired");
                return "redirect:/error";
            case "valid":
                redirectAttributes.addFlashAttribute("message", "Token valid");
                return "redirect:/login";
            default:
                redirectAttributes.addFlashAttribute("message", "Invalid verification token");
                return "redirect:/error";
        }
    }
    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm(){
        return "auth/forgot-password";
    }

//    @PostMapping("/forgot-password")
//    public String resetPasswordRequest(HttpServletRequest request, Model model){
//        String email = request.getParameter("email");
//        Optional<User> user= userService.findByEmail(email);
//        if (user.isEmpty()){
//            return  "redirect:/registration/forgot-password-request?not_found";
//        }
//        String passwordResetToken = UUID.randomUUID().toString();
//        passwordResetTokenService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
//        //send password reset verification email to the user
//        String url = UrlUtil.getApplicationUrl(request)+"/registration/password-reset-form?token="+passwordResetToken;
//        try {
//            eventListener.sendPasswordResetVerificationEmail(url);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            model.addAttribute("error", e.getMessage());
//        }
//        return "redirect:/registration/forgot-password-request?success";
//    }
@PostMapping("/forgot-password")
public String resetPasswordRequest(HttpServletRequest request, Model model) {
    String email = request.getParameter("email");
    logger.info("Received forgot password request for email: {}", email);

    Optional<User> userOptional = userService.findByEmail(email);
    if (userOptional.isEmpty()) {
        logger.warn("User not found for email: {}", email);
        return "redirect:/registration/forgot-password-request?not_found";
    }

    User user = userOptional.get();
    String passwordResetToken = UUID.randomUUID().toString();
    passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    logger.info("Password reset token created for user: {}", user.getEmail());

    // Send password reset verification email to the user
    String url = UrlUtil.getApplicationUrl(request) + "/registration/password-reset-form?token=" + passwordResetToken;
    try {
        eventListener.sendPasswordResetVerificationEmail(url, user);
        logger.info("Password reset email sent to: {}", user.getEmail());
        return "redirect:/registration/forgot-password-request?success";
    } catch (MessagingException | UnsupportedEncodingException e) {
        logger.error("Error sending password reset email to: {}", user.getEmail(), e);
        model.addAttribute("error", e.getMessage());
        return "redirect:/registration/forgot-password-request?error";
    }
}

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model){
        model.addAttribute("token", token);
        return "auth/password-reset-form";
    }
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(theToken);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){
            return "redirect:/error?invalid_token";
        }
        Optional<User> theUser = passwordResetTokenService.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()){
            passwordResetTokenService.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/error?not_found";
    }
}
