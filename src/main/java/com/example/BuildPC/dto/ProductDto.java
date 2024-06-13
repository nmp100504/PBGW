package com.example.BuildPC.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NotEmpty
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    @NotEmpty(message = "The Product Name is required")
    private String productName;
    private String productSlug;
    @NotEmpty(message = "The Product Original Price is required")
    private float productOriginalPrice;
    @NotEmpty(message = "The Product Sale Price is required")
    private float productSalePrice;
    private String productDesc;
    @NotEmpty(message = "The Units In Stock is required")
    private Integer unitsInStock;
    @NotEmpty(message = "The Units In Order is required")
    private Integer unitsInOrder;

}
