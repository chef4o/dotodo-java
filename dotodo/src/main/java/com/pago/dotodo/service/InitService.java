package com.pago.dotodo.service;

import com.pago.dotodo.model.entity.RoleEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.RoleRepository;
import com.pago.dotodo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class InitService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public InitService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        if (this.roleRepository.count() == 0) {
            initRoles();
        }

        if (this.userRepository.count() == 0) {
            initSuperAdmin();
            initModerator();
            initNormal();
        }
    }

    private void initRoles() {
        Arrays.stream(RoleEnum.values())
                .map(roleEnum -> new RoleEntity().setRole(roleEnum))
                .forEach(roleRepository::save);
    }

    private void initNormal() {
        UserEntity user = new UserEntity()
                .setEmail("normal@abv.bg")
                .setUsername("normal")
                .setFirstName("Stef")
                .setPassword(passwordEncoder.encode("test123"))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.NORMAL)
                        .orElseThrow());
        userRepository.save(user);
    }

    private void initSuperAdmin() {
        UserEntity user = new UserEntity()
                .setEmail("admin@abv.bg")
                .setUsername("admin")
                .setFirstName("StefSupAdm")
                .setPassword(passwordEncoder.encode("test123"))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.SUPER_ADMIN)
                        .orElseThrow());
        userRepository.save(user);
    }

    private void initModerator() {
        UserEntity user = new UserEntity()
                .setEmail("moderator@abv.bg")
                .setUsername("moderator")
                .setFirstName("StefMod")
                .setPassword(passwordEncoder.encode("test123"))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.MODERATOR)
                        .orElseThrow());
        userRepository.save(user);
    }
}
