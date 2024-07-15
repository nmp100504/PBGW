package com.example.BuildPC.utils;

import com.example.BuildPC.configuration.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static CustomUserDetails getCurrentUser() {
        Object principle = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principle instanceof CustomUserDetails) {
            return (CustomUserDetails) principle;
        }
        return null;
    }
}
