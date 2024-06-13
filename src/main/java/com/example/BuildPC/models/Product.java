package com.example.BuildPC.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private String productName;

    @Column(name = "product_slug")
    private String productSlug;

    @Column(name = "product_original_price")
    private float productOriginalPrice;

    @Column(name = "product_sale_price")
    private float productSalePrice;

    @Column(name = "product_desc")
    private String productDesc;

    @Column(name = "units_in_stock")
    private Integer unitsInStock;

    @Column(name = "units_in_order")
    private Integer unitsInOrder;

    @Column(name = "product_status")
    private Boolean productStatus;

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

}
