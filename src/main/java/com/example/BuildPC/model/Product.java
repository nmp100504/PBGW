package com.example.BuildPC.model;


import com.example.BuildPC.service.ProductImageService;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @Column(name = "product_name")
    @NotEmpty(message = "The Product Name is required")
    @Size(max = 100, message = "The Product Name does not exceed 100 characters")
    private String productName;

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "product_original_price")
    @NotNull(message =  "The Product Original Price is required and must be number!")
    @DecimalMin(value = "0.0", inclusive = false, message = "The Product Original Price is must greater than 0!")
    private float productOriginalPrice;

    @Column(name = "product_sale_price")
    @DecimalMin(value = "0.0", inclusive = false, message = "The Product Sale Price is must greater than 0!")
    private float productSalePrice;

    @Column(name = "product_desc")
    @NotEmpty(message = "The Product Description is required")
    private String productDesc;

    @Column(name = "units_in_stock")
    @NotNull(message =  "The Units In Stock is required and must be number!")
    @Min(value = 1, message = "The Units In Stock is must greater than equal 1!")
    private Integer unitsInStock;

    @Column(name = "units_in_order")
    private Integer unitsInOrder;

    @Column(name = "product_status")
    private boolean productStatus;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;


    @ManyToOne()
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductImage other = (ProductImage) obj;
        return Objects.equals(id, other.getId());
    }

}
