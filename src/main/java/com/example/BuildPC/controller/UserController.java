package com.example.BuildPC.controller;


import com.example.BuildPC.dtos.UserDto;
import com.example.BuildPC.models.User;
import com.example.BuildPC.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping({"","/"})
    public String showUserList(Model model){
        List<User> user = repository.findAll();
        model.addAttribute("user", user);
        return "users/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "users/createUser";
    }

    @PostMapping("/create")
    public String createuUser(@Validated @ModelAttribute UserDto userDto, BindingResult result){

        if(result.hasErrors()){
            return "users/createUser";
        }

        User user = new User();
        user.setFistName(userDto.getFistName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        repository.save(user);
        return "redirect:/users";
    }




    @GetMapping("/delete")
    public  String deleteProduct(@RequestParam int id){
        try{
            User user = repository.findById(id).get();
            //delete product image
            repository.delete(user);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/users";
        }
        return "redirect:/users";
    }
}
