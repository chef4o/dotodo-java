package com.pago.dotodo.service;

import com.pago.dotodo.model.entity.User;
import com.pago.dotodo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (this.userRepository.count() == 0) {
            initAdmin();
        }
    }

    private void initAdmin() {
        User user = new User()
                .setEmail("admin@abv.bg")
                .setUsername("admin")
                .setFirstName("Stef")
                .setPassword(passwordEncoder.encode("test123"))
                .setRole(4);
        
        userRepository.save(user);
    }
}
