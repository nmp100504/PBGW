package com.example.BuildPC.service;

import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Specifications;
import com.example.BuildPC.repository.SpecificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    @Transactional
    public Specifications saveSpecification(Specifications specification) {
        return specificationRepository.save(specification);
    }

    public Specifications getSpecification(Long specId) {
        return specificationRepository.findById(specId).orElse(null);
    }
    public List<Specifications> getAllSpecificationsByCategory(Category cate) {
        return specificationRepository.findAllByCategory(cate);
    }
}
