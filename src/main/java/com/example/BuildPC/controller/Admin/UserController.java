package com.example.BuildPC.controller.Admin;

import com.example.BuildPC.service.UserService;
import com.example.BuildPC.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "auth/users";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        Optional<User> user = userService.findById(id);
        model.addAttribute("user", user.get());
        return "auth/update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/users?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/users?delete_success";
    }


}
