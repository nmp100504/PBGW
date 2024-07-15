package com.example.BuildPC.controller.Admin;


import com.example.BuildPC.service.AdminService;
import com.example.BuildPC.dto.UserDto;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashBoard")
public class AdminController {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;


    //Hien thi thong tin nguoi dung o trang dashboard
    @GetMapping({"", "/"})
    public String showUserListAdminDashboard(Model model,
                                             @RequestParam(value = "searchByEmailOrPhone", required = false) String searchByEmailOrPhone,
                                             @RequestParam(value = "status", required = false) String status) {
        List<User> user = adminService.findAll();

        if (searchByEmailOrPhone != null && !searchByEmailOrPhone.isEmpty() && status != null && !status.isEmpty()) {
            boolean isActive = status.equals("active");
            user = adminService.searchByUserEmailOrPhoneAndIsEnabled(searchByEmailOrPhone, isActive);
        } else if (searchByEmailOrPhone != null && !searchByEmailOrPhone.isEmpty()) {
            user = adminService.searchByUserEmailOrPhone(searchByEmailOrPhone);
            model.addAttribute("searchByEmailOrPhone", searchByEmailOrPhone);
        } else if (status != null && !status.isEmpty()) {
            boolean isActive = status.equals("active");
            user = adminService.listByUserIsEnabled(isActive);
            model.addAttribute("status", status);
        }

        model.addAttribute("user", user);
        long totalAccounts = adminService.countTotalAccounts();
        model.addAttribute("totalAccounts", totalAccounts);
        long activeAccounts = adminService.countActiveAccounts();
        model.addAttribute("activeAccounts", activeAccounts);
        long inActiveAccounts = adminService.countInactiveAccounts();
        model.addAttribute("inActiveAccounts", inActiveAccounts);

        return "dashBoard/index";
    }
    @GetMapping("/charts")
    public String showChart(Model model){
        long totalAccounts = adminService.countTotalAccounts();
        long activeAccounts = adminService.countActiveAccounts();
        long inActiveAccounts = adminService.countInactiveAccounts();

        model.addAttribute("totalAccounts", totalAccounts);
        model.addAttribute("activeAccounts", activeAccounts);
        model.addAttribute("inActiveAccounts", inActiveAccounts);

        return "dashBoard/charts";
    }

    // Hien thi danh sach thong tin nguoi dung trong trang tables
    @GetMapping("/tables")
    public String showUserListAdminDashboardTableDetail(Model model,@Param("searchByEmailOrPhone") String searchByEmailOrPhone, @Param("status") String status){
        List<User> users = adminService.findAll();
        if (searchByEmailOrPhone != null && !searchByEmailOrPhone.isEmpty() && status != null && !status.isEmpty()) {
            boolean isActive = status.equals("active");
            users = adminService.searchByUserEmailOrPhoneAndIsEnabled(searchByEmailOrPhone, isActive);
        } else if (searchByEmailOrPhone != null && !searchByEmailOrPhone.isEmpty()) {
            users = adminService.searchByUserEmailOrPhone(searchByEmailOrPhone);
            model.addAttribute("searchByEmailOrPhone", searchByEmailOrPhone);
        } else if (status != null && !status.isEmpty()) {
            boolean isActive = status.equals("active");
            users = adminService.listByUserIsEnabled(isActive);
            model.addAttribute("status", status);
        }
        model.addAttribute("users", users);
        long totalAccounts = adminService.countTotalAccounts();
        model.addAttribute("totalAccounts", totalAccounts);
        long activeAccounts = adminService.countActiveAccounts();
        model.addAttribute("activeAccounts", activeAccounts);
        long inActiveAccounts = adminService.countInactiveAccounts();
        model.addAttribute("inActiveAccounts", inActiveAccounts);
        return "dashBoard/tables";
    }
    // Hien thi danh sach thong tin nguoi dung trong trang tables theo trang thai tai khoan dang hoat dong
    @GetMapping("/tables/activeAccount")
    public String showActiveUserListAdminDashboardTableDetail(Model model){
        List<User> users = adminService.findActiveAccounts();
        model.addAttribute("users", users);
        long totalAccounts = adminService.countTotalAccounts();
        model.addAttribute("totalAccounts", totalAccounts);
        long activeAccounts = adminService.countActiveAccounts();
        model.addAttribute("activeAccounts", activeAccounts);
        long inActiveAccounts = adminService.countInactiveAccounts();
        model.addAttribute("inActiveAccounts", inActiveAccounts);
        return "dashBoard/tables";
    }
    // Hien thi danh sach thong tin nguoi dung trong trang tables theo trang thai tai khoan khong hoat dong
    @GetMapping("/tables/inActiveAccount")
    public String showInActiveUserListAdminDashboardTableDetail(Model model){
        List<User> users = adminService.findInactiveAccounts();
        model.addAttribute("users", users);
        long totalAccounts = adminService.countTotalAccounts();
        model.addAttribute("totalAccounts", totalAccounts);
        long activeAccounts = adminService.countActiveAccounts();
        model.addAttribute("activeAccounts", activeAccounts);
        long inActiveAccounts = adminService.countInactiveAccounts();
        model.addAttribute("inActiveAccounts", inActiveAccounts);
        return "dashBoard/tables";
    }


    // Tao moi thong tin nguoi dung o trang dashboard
    @GetMapping("/create")
    public String showCreateAdminDashboard(Model model){
        UserDto userDto = new UserDto();
        userDto.setEnabled(true);
        model.addAttribute("userDto", userDto);
        return "dashBoard/createUser";
    }

    @PostMapping("/create")
    public String createUserAdminDashboard(@Valid @ModelAttribute UserDto userDto, BindingResult result){
        if(adminService.existsByUserEmail(userDto.getEmail())){
            result.addError(new FieldError("userDto", "email", "Email already exists"));
        }
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
        userDto.setEnabled(true);
        model.addAttribute("userDto", userDto);
        return "dashBoard/createUser";
    }

    @PostMapping("/tables/create")
    public String createCreateAdminDashboardTableDetail(@Valid @ModelAttribute UserDto userDto, BindingResult result){
        if(adminService.existsByUserEmail(userDto.getEmail())){
            result.addError(new FieldError("userDto", "email", "Email already exists"));
        }
        if(result.hasErrors()){
            return "dashBoard/createUser";
        }
        if(userDto.getPassword() == null && userDto.getPassword().isEmpty()){
            result.addError(new FieldError("userDto", "password", "Please select a file"));
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
            userDto.setEnabled(user.isEnabled());
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
            if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }

            user.setPhone(userDto.getPhone());
            user.setRole(Role.valueOf(userDto.getRole()));
            user.setEnabled(userDto.isEnabled());
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
            userDto.setEnabled(user.isEnabled());

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
            if(userDto.getPassword() != null && !userDto.getPassword().isEmpty()){
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            user.setPhone(userDto.getPhone());
            user.setRole(Role.valueOf(userDto.getRole()));
            user.setEnabled(userDto.isEnabled());
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
//            adminService.deleteUserById(adminService.findUserById(id).getId());
            adminService.deactivateAccountById(id);

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
//            adminService.deleteUserById(adminService.findUserById(id).getId());
            adminService.deactivateAccountById(id);

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/dashBoard/tables";
        }
        return "redirect:/dashBoard/tables";
    }
}
