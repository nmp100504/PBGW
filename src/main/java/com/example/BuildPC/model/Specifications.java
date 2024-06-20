package com.example.BuildPC.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Specifications")
public class Specifications {

    @Id
    @Column(name = "spec_id")
    private String specId;

    @Column(name = "spec_name")
    private String specName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

}

