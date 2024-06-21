package com.example.BuildPC.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "ProductSpecifications")
public class ProductSpecifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "spec_id", referencedColumnName = "spec_id")
    private Specifications specification;

    @Column(name = "spec_value")
    private String specValue;

}