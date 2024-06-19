package com.example.BuildPC.configuration;

import com.example.BuildPC.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private String userName;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;
    @Setter
    @Getter
    private String firstName;
    @Setter
    @Getter
    private String lastName;
    @Setter
    @Getter
    private String email;

    public CustomUserDetails(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return
                this.firstName + this.lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
