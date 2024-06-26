package com.example.BuildPC.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final String uploadDir = "src/main/resources/static/uploads/";

    public String saveProfileImage(MultipartFile profileImage) throws IOException {
        if (profileImage.isEmpty()) {
            return null;
        }

        // Create the uploads directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file locally
        String originalFilename = profileImage.getOriginalFilename();
        String filePath = uploadDir + System.currentTimeMillis() + "_" + originalFilename;
        Path path = Paths.get(filePath);
        Files.write(path, profileImage.getBytes());

        return System.currentTimeMillis() + "_" + originalFilename; // Ensure this path is accessible by the frontend
    }

    public byte[] readProfileImage(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}