package com.example.BuildPC.dtos;

import com.example.BuildPC.models.Product;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

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
