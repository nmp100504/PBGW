package com.example.BuildPC.controller;

import com.example.BuildPC.model.User;
import com.example.BuildPC.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "LandingPage/login_page";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "LandingPage/registration_page";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login?success";
    }
}
