package com.example.BuildPC.service;

import com.example.BuildPC.model.Brand;
import com.example.BuildPC.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findByBranId(int id) {
        return brandRepository.findById(id).get();
    }
}
