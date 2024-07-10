package com.example.BuildPC.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NaturalId(mutable = true)
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name ="phone")
    private String phone;

    @Column(name ="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name ="isEnabled")
    private boolean isEnabled;



    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @Column(name = "profile_image")
    private String profileImage;

    public User(String email, String firstName, String lastName, String password, String phone, Role role) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.phone = this.phone;
        this.role = role;
    }



}
