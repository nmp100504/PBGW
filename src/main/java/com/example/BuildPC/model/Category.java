package com.example.BuildPC.model;


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
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category_slug")
    private String categorySlug;

    @Column(name = "category_description")
    private String categoryDesc;

    @Column(name = "category_image")
    private String categoryImage;

    @Column(name = "category_status")
    private boolean categoryStatus;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    @OneToMany(mappedBy = "category")
    private List<Specifications> specifications;

}
