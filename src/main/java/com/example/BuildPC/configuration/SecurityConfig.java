package com.example.BuildPC.configuration;

import com.example.BuildPC.service.CustomUserDetailsService;
import com.example.BuildPC.model.Role;
import com.example.BuildPC.model.User;
import com.example.BuildPC.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.slf4j.Logger;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","login", "/error", "/registration/**","/homepage"
                                ,"/product/**","/category/**","/search/**","/sort/**").permitAll()
                        .requestMatchers("/dashBoard/**").hasRole("ADMIN")
                        .requestMatchers("/ManagerDashBoard/**").hasRole("MANAGER")
                        .requestMatchers("/posts").hasRole("MARKETING")
                        .requestMatchers(staticResources()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/homepage", true)
                        .permitAll())
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret").tokenValiditySeconds(86400))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    private static String[] staticResources() {
        return new String[]{
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/assets/**" ,
                "/assetsLandingPage/**",
                "/assetsDashboard/**",
                "/assetsDashboard/vendor/**",
                "/images/**",
                "/images/Category/**",
                "/images/Product/**",
                "/images/ProductCover/**",
                "/uploads/**"
        };
    }

    @Bean
    public CommandLineRunner setupDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("manager@example.com").isEmpty()) {
                User manager = new User();
                manager.setEmail("manager@example.com");
                manager.setPassword(passwordEncoder.encode("manager123"));
                manager.setRole(Role.MANAGER);
                manager.setEnabled(true);

                userRepository.save(manager);
            }

            if (userRepository.findByEmail("marketing@example.com").isEmpty()) {
                User marketing = new User();
                marketing.setEmail("marketing@example.com");
                marketing.setPassword(passwordEncoder.encode("123"));
                marketing.setRole(Role.MARKETING);
                marketing.setEnabled(true);

                userRepository.save(marketing);
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            Logger logger = LoggerFactory.getLogger(this.getClass());

            if (exception instanceof DisabledException) {
                logger.debug("DisabledException caught, redirecting to /login?account_not_enabled");

                response.sendRedirect("/login?account_not_enabled");
            } else {
                logger.debug("Other authentication exception: {}", exception.getMessage());
                response.sendRedirect("/login?error");
            }
        };
    }
}


