package com.example.BuildPC.controller;


import com.example.BuildPC.Service.AdminService;
import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
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

//    @Autowired
//    private AdminRepository repository;
    @Autowired
    private AdminService adminService;


    //Hien thi thong tin nguoi dung o trang dashboard
    @GetMapping({"","/"})
    public String showUserListAdminDashboard(Model model){
        List<User> user = adminService.findAll();
        model.addAttribute("user", user);
        return "dashBoard/index";
    }

    // Hien thi danh sach thong tin nguoi dung trong trang tables
    @GetMapping("/tables")
    public String showUserListAdminDashboardTableDetail(Model model){
        List<User> users = adminService.findAll();
        model.addAttribute("users", users);
        return "dashBoard/tables";
    }


    // Tao moi thong tin nguoi dung o trang dashboard
    @GetMapping("/create")
    public String showCreateAdminDashboard(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "dashBoard/createUser";
    }

    @PostMapping("/create")
    public String createUserAdminDashboard(@Valid @ModelAttribute UserDto userDto, BindingResult result){

        if(result.hasErrors()){
            return "dashBoard/createUser";
        }
        adminService.save(userDto);
        return "redirect:/dashBoard";
    }

    //Tao moi thong tin nguoi dung o trang tables
    @GetMapping("/tables/create")
    public String showCreateAdminDashboardTableDetail(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return "dashBoard/createUser";
    }

    @PostMapping("/tables/create")
    public String createCreateAdminDashboardTableDetail(@Valid @ModelAttribute UserDto userDto, BindingResult result){

        if(result.hasErrors()){
            return "dashBoard/createUser";
        }

       adminService.save(userDto);
        return "redirect:/dashBoard/tables";
    }

    @GetMapping("/edit")
    public String showEditPage(Model model, @RequestParam int id){

        try{
            User user = adminService.findUserById(id);
            model.addAttribute("user", user);

            UserDto userDto = new UserDto();
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setPhone(user.getPhone());
            userDto.setRole(String.valueOf(user.getRole()));

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
            User user = adminService.findUserById(id);
            model.addAttribute("user", user);
            if(result.hasErrors()){
                return "dashBoard/editUser";
            }

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());
            user.setRole(Role.valueOf(userDto.getRole()));
            adminService.updateUser(user);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard";
        }
        return "redirect:/dashBoard";
    }

    @GetMapping("/tables/edit")
    public String showEditPage1(Model model, @RequestParam int id){

        try{
            User user = adminService.findUserById(id);
            model.addAttribute("user", user);

            UserDto userDto = new UserDto();
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setPassword(user.getPassword());
            userDto.setPhone(user.getPhone());
            userDto.setRole(String.valueOf(user.getRole()));

            model.addAttribute("userDto",userDto);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard/tables";
        }

        return "dashBoard/editUser";
    }

    @PostMapping("/tables/edit")
    public String updateUser1(Model model, @RequestParam int id, @Valid @ModelAttribute UserDto userDto,
                             BindingResult result){
        try {
            User user = adminService.findUserById(id);
            model.addAttribute("user", user);
            if(result.hasErrors()){
                return "dashBoard/editUser";
            }

            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());
            user.setRole(Role.valueOf(userDto.getRole()));

            adminService.updateUser(user);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard/tables";
        }
        return "redirect:/dashBoard/tables";
    }

    @GetMapping("/delete")
    public  String deleteUser(@RequestParam long id){
        try{
//            User user = repository.findById(id).get();

            adminService.deleteUserById(adminService.findUserById(id).getId());

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard";
        }
        return "redirect:/dashBoard";
    }
    @GetMapping("tables/delete")
    public  String deleteUser1(@RequestParam long id){
        try{
//            User user = adminService.findUserById(id);

            adminService.deleteUserById(adminService.findUserById(id).getId());

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard/tables";
        }
        return "redirect:/dashBoard/tables";
    }
}
