package com.example.BuildPC.controller.Manager;

import com.example.BuildPC.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderDetailController {

    @Autowired
    OrderDetailService orderDetailService;


}
