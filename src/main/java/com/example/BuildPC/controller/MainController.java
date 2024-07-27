package com.example.BuildPC.controller;

import com.example.BuildPC.configuration.CustomUserDetails;
import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Product;
import com.example.BuildPC.model.Status;
import com.example.BuildPC.model.User;
import com.example.BuildPC.service.*;
import com.example.BuildPC.service.implementation.ProductServiceImpl;
import com.example.BuildPC.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.Binding;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @GetMapping("/ManagerDashBoard")
    public String showManagerDashBoard(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,Model model) {
        long totalProducts = productService.countTotalProducts();
        model.addAttribute("totalProducts", totalProducts);
        long activeProducts = productService.countActiveProducts();
        model.addAttribute("activeProducts", activeProducts);
        long inActiveProducts = productService.countInActiveProducts();
        model.addAttribute("inActiveProducts", inActiveProducts);
        long totalCategories = categoryService.countTotalCategories();
        model.addAttribute("totalCategories", totalCategories);
        long activeCategories = categoryService.countActiveCategories();
        model.addAttribute("activeCategories", activeCategories);
        long inActiveCategories = categoryService.countInActiveCategories();
        model.addAttribute("inActiveCategories", inActiveCategories);
        model.addAttribute("OrderList", orderService.listAllOrder());



        if (date == null) {
            date = new Date();
            model.addAttribute("countWAIT", orderService.countOrderByStatus(Status.WAIT));
            System.out.println("----------------------------------------------------------");
            System.out.println(orderService.countOrderByStatus(Status.WAIT));
            System.out.println("----------------------------------------------------------");
            model.addAttribute("countINPROGRESS", orderService.countOrderByStatus(Status.IN_PROGRESS));
            System.out.println("----------------------------------------------------------");
            System.out.println(orderService.countOrderByStatus(Status.IN_PROGRESS));
            System.out.println("----------------------------------------------------------");
            model.addAttribute("countDONE", orderService.countOrderByStatus(Status.DONE));
            model.addAttribute("countCANCEL", orderService.countOrderByStatus(Status.CANCEL));
        } else {
            model.addAttribute("countWAIT", orderService.countOrdersByStatusAndDate(Status.WAIT, date));
            model.addAttribute("countINPROGRESS", orderService.countOrdersByStatusAndDate(Status.IN_PROGRESS, date));
            model.addAttribute("countDONE", orderService.countOrdersByStatusAndDate(Status.DONE, date));
            model.addAttribute("countCANCEL", orderService.countOrdersByStatusAndDate(Status.CANCEL, date));
            model.addAttribute("selectedDate", date);
        }
        model.addAttribute("monthlyTotal", orderService.getTotalOrderValueByMonth());
        model.addAttribute("monthlyOrderCount", orderService.getOrderCountByMonth());
        return "Manager/managerDashBoard";
    }

    @GetMapping("/ManagerDashBoard/chartsManager")
    public String showChart(Model model){
        long activeProducts = productService.countActiveProducts();
        long inActiveProducts  = productService.countInActiveProducts();
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("inActiveProducts", inActiveProducts);
        long activeCategories = categoryService.countActiveCategories();
        model.addAttribute("activeCategories", activeCategories);
        long inActiveCategories = categoryService.countInActiveCategories();
        model.addAttribute("inActiveCategories", inActiveCategories);
        long totalProducts = productService.countTotalProducts();
        model.addAttribute("totalProducts", totalProducts);
        long totalCategories = categoryService.countTotalCategories();
        model.addAttribute("totalCategories", totalCategories);

        model.addAttribute("monthlyTotal", orderService.getTotalOrderValueByMonth());
        model.addAttribute("monthlyOrderCount", orderService.getOrderCountByMonth());

        return "Manager/chartsManager";
    }


    @GetMapping("/login")
    public String login() {
        return "auth/login_page";
    }

    @GetMapping({"/", "/homepage"})
    public String showLandingPage(Model model) {
        List<Category> categoryList = categoryService.findAll();
        List<Product> productList = productService.listActiveProduct(true);
        List<Product> reverseProductList = productService.listActiveProduct(true);
        Collections.reverse(reverseProductList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("productList", productList);
        model.addAttribute("reverseProductList", reverseProductList);
        model.addAttribute("bestSeller",productServiceImpl.findTop10());
        return "LandingPage/index_1";
    }

    @GetMapping("/account")
    public String showAccountPage(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account_information";
            }
        return "LandingPage/index_1";
    }



    @GetMapping("/change-password")
    public String changePassword(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> currentUser = userService.findByEmail(email);
            Optional<User> user = userService.findByEmail(currentUser.get().getEmail());
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/change_password";
            }
        return "redirect:/login";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
                    model.addAttribute("error", "Old password is incorrect");
                    return "auth/change_password";
                }
                if (!newPassword.equals(confirmPassword)) {
                    model.addAttribute("error", "New passwords do not match");
                    return "auth/change_password";
                }
                user.setPassword((newPassword));
                userService.updateUser(user, newPassword);
                return "redirect:/account";
            }

        return "redirect:/login";
    }

    @GetMapping("/update-user-info")
    public String editInfo(Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                return "auth/account_information";
            }

        return "LandingPage/index_1";
    }
    @PostMapping("/update-user-info")
    public String updateUserInfo(@RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("avatar") MultipartFile avatar,
                                 Model model) {
        String email = Objects.requireNonNull(SecurityUtils.getCurrentUser()).getEmail();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                userService.updateUser(user, avatar);
                model.addAttribute("user", user);
                return "auth/account_information";
            }

        return "redirect:/login";
    }
}
