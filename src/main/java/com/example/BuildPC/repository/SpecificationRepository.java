package com.example.BuildPC.repository;

import com.example.BuildPC.model.Category;
import com.example.BuildPC.model.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecificationRepository extends JpaRepository<Specifications, Long> {
    List<Specifications> findAllByCategory(Category category);
}
