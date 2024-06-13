package com.example.BuildPC.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @NotEmpty(message = "The Category Name is required")
    private String categoryName;

    private String categorySlug;

    private String categoryDesc;

    private MultipartFile categoryImage;


}
