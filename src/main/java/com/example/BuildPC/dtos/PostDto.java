package com.example.BuildPC.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class PostDto {

    @NotEmpty(message = "The title is required")
    private String title;

    @Size(min = 10, message = "The content should be at least 10 characters")
    @Size(max = 500000, message = "The content cannot exceed 500000 characters")
    private String content;

    private MultipartFile imageFile;

    // Add user information
    private Integer userId; // Assuming user ID is passed from the client

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
