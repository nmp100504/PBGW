package com.example.BuildPC.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentStatusController {

    @GetMapping("/payment/PaymentSuccess")
    public String paymentSuccess() {
        return "payment/PaymentSuccess";
    }

    @GetMapping("/payment/PaymentFail")
    public String paymentFail() {
        return "payment/PaymentFail";
    }
}
