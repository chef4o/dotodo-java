package com.pago.dotodo.service;

import com.pago.dotodo.model.entity.RoleEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.RoleRepository;
import com.pago.dotodo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class InitService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(InitService.class);

    @Value("${app.default.password}")
    String defaultPassword;

    public InitService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        logger.info("Initializing roles and users");

        if (this.roleRepository.count() == 0) {
            logger.info("No roles found, initializing roles");
            initRoles();
        }

        if (this.userRepository.count() == 0) {
            logger.info("No users found, initializing default users");
            initSuperAdmin();
            initModerator();
            initNormal();
        }
    }

    private void initRoles() {
        Arrays.stream(RoleEnum.values())
                .map(roleEnum -> new RoleEntity().setRole(roleEnum))
                .forEach(roleRepository::save);
        logger.info("Roles initialized");
    }

    private void initNormal() {
        UserEntity user = new UserEntity()
                .setEmail("normal@abv.bg")
                .setUsername("normal")
                .setFirstName("Stef")
                .setPassword(passwordEncoder.encode(defaultPassword))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.NORMAL)
                        .orElseThrow());
        userRepository.save(user);
        logger.info("Normal user initialized");
    }

    private void initSuperAdmin() {
        UserEntity user = new UserEntity()
                .setEmail("admin@abv.bg")
                .setUsername("admin")
                .setFirstName("StefSupAdm")
                .setPassword(passwordEncoder.encode(defaultPassword))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.SUPER_ADMIN)
                        .orElseThrow());
        userRepository.save(user);
        logger.info("Super Admin user initialized");
    }

    private void initModerator() {
        UserEntity user = new UserEntity()
                .setEmail("moderator@abv.bg")
                .setUsername("moderator")
                .setFirstName("StefMod")
                .setPassword(passwordEncoder.encode(defaultPassword))
                .addRole(roleRepository
                        .findRoleEntityByRole(RoleEnum.MODERATOR)
                        .orElseThrow());
        userRepository.save(user);
        logger.info("Moderator user initialized");
    }
}
