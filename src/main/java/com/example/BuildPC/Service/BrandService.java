package com.example.BuildPC.Service;

import com.example.BuildPC.dtos.BrandDto;
import com.example.BuildPC.dtos.ProductDto;
import com.example.BuildPC.models.Brand;
import com.example.BuildPC.models.Product;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findByBranId(int id);
}
