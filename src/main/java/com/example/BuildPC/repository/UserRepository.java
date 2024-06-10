package com.example.BuildPC.repository;

import com.example.BuildPC.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Modifying
    @Query(value = "UPDATE User u set u.firstName =:firstName,"+
            " u.lastName =:lastName," + "u.email =:email where u.id =:id")
    void update(String firstName, String lastName, String email, Long id);
}
