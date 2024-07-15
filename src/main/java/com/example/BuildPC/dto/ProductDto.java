package com.example.BuildPC.dto;

import jakarta.validation.constraints.NotEmpty;
import com.example.BuildPC.model.Category;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotEmpty(message = "The Product Name is required")
    @Size(max = 100, message = "The Product Name does not exceed 100 characters")
    private String productName;
    private String productSlug;
    @NotNull(message =  "The Product Original Price is required and must be number!")
    @DecimalMin(value = "0.0", inclusive = false, message = "The Product Original Price is must greater than 0!")
    private float productOriginalPrice;
    //@NotNull(message = "The Product Sale Price is required and must be number!")
    @DecimalMin(value = "0.0", inclusive = false, message = "The Product Sale Price is must greater than 0!")
    private float productSalePrice;
    @NotEmpty(message = "The Product Description is required")
    private String productDesc;

    @NotNull(message =  "The Units In Stock is required and must be number!")
    @Min(value = 1, message = "The Units In Stock is must greater than equal 1!")
    private Integer unitsInStock;
    private Integer unitsInOrder;
    private boolean productStatus;

    @NotNull(message =  "The Category is required")
    private Integer categoryId;
    @NotNull(message =  "The Brand is required")
    private Integer brandId;

    private List<MultipartFile> productImages;

}
