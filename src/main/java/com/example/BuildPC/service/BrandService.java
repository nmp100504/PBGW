package com.example.BuildPC.service;

import com.example.BuildPC.dtos.BrandDto;
import com.example.BuildPC.dto.ProductDto;
import com.example.BuildPC.model.Brand;
import com.example.BuildPC.model.Product;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findByBranId(int id);
}
