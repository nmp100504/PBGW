package com.example.BuildPC.dtos;

import com.example.BuildPC.models.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    @NotEmpty(message = "The Product Name is required")
    private String productName;
    private String productSlug;

    private float productOriginalPrice;

    private float productSalePrice;

    private String productDesc;

    private Integer unitsInStock;

    private Integer unitsInOrder;


    private Integer categoryId;

    private Integer brandId;


}
