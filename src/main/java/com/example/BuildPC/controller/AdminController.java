package com.example.BuildPC.controller;


import com.example.BuildPC.dtos.UserDto;
import com.example.BuildPC.models.User;
import com.example.BuildPC.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashBoard")
public class AdminController {

    @Autowired
    private UserRepository repository;

    @GetMapping({"","/"})
    public String showUserList(Model model){
        List<User> user = repository.findAll();
        model.addAttribute("user", user);
        return "dashBoard/index";
    }


    @GetMapping("/tables")
    public String showUserList1(Model model){
        List<User> users = repository.findAll();
        model.addAttribute("users", users);
        return "dashBoard/tables";
    }


    @GetMapping("/create")
    public String showCreatePage(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "dashBoard/createUser";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute UserDto userDto, BindingResult result){

        if(result.hasErrors()){
            return "dashBoard/createUser";
        }

        User user = new User();
        user.setFirstName(userDto.getFistName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());
        repository.save(user);
        return "redirect:/dashBoard";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){

        try{
            User user = repository.findById(id).get();
            model.addAttribute("user", user);

            UserDto userDto = new UserDto();
            userDto.setFistName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setPhone(user.getPhone());
            userDto.setRole(user.getRole());

            model.addAttribute("userDto",userDto);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard";
        }

        return "dashBoard/editUser";
    }

    @PostMapping("/edit")
    public String updateUser(Model model, @RequestParam int id, @Valid @ModelAttribute UserDto userDto,
                                BindingResult result){
        try {
            User user = repository.findById(id).get();
            model.addAttribute("user", user);
            if(result.hasErrors()){
                return "dashBoard/editUser";
            }

            user.setFirstName(userDto.getFistName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());
            user.setRole(userDto.getRole());

            repository.save(user);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard";
        }
        return "redirect:/dashBoard";
    }


    @GetMapping("/delete")
    public  String deleteUser(@RequestParam int id){
        try{
            User user = repository.findById(id).get();
            //delete product image
            repository.delete(user);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard";
        }
        return "redirect:/dashBoard";
    }
}
