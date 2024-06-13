package com.example.BuildPC.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {

    @NotEmpty(message = "The Brand Name is required")
    private String brandName;

    private String brandDesc;
}
