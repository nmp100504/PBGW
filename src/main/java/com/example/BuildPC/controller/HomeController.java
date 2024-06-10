package com.example.BuildPC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String homePage(){
        return "auth/home";
    }
    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }



    @GetMapping("/admin")
    public String admin(){
        return "hello admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "hello manager";
    }
}
