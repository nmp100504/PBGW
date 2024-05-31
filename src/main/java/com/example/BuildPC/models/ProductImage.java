package com.example.BuildPC.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;


    @Column(name = "image_filename")
    private String imageFileName;

    @ManyToOne()
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;
}
