package com.example.BuildPC.model;

import lombok.Getter;
import lombok.Setter;


public class SpecificationWrapper {
    // Getters and setters
    private ProductSpecifications productSpecification;
    private Specifications specification;

    public SpecificationWrapper(ProductSpecifications productSpecification, Specifications specification) {
        this.productSpecification = productSpecification;
        this.specification = specification;
    }

    public Specifications getSpecification() {
        return specification;
    }

    public void setSpecification(Specifications specification) {
        this.specification = specification;
    }

    public ProductSpecifications getProductSpecification() {
        return productSpecification;
    }

    public void setProductSpecification(ProductSpecifications productSpecification) {
        this.productSpecification = productSpecification;
    }
}
