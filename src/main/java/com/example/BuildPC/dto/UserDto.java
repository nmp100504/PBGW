package com.example.BuildPC.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "The email is required")
    private String email;
    @NotEmpty(message = "The fistName is required")
    private String firstName;
    @NotEmpty(message = "The lastName is required")
    private String lastName;
    @NotEmpty(message = "The password is required")
    private String password;
    @Size(max = 10, message = "The description cannot exceed 10 characters")
    private String phone;

    private boolean isEnabled;
    // Getter method for 'enabled'
    public boolean isEnabled() {
        return isEnabled;
    }

    // Setter method for 'enabled'
    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @NotEmpty(message = "The role is required")
    private String role;

    private byte[] avatar;

    public String getAvatarDataBase64() {
        return Base64.getEncoder().encodeToString(this.avatar);
    }
}
