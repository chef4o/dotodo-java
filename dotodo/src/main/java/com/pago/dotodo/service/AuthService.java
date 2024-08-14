package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.UserRepository;
import com.pago.dotodo.security.AppUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailsService appUserDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper,
                       AppUserDetailsService appUserDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailsService = appUserDetailsService;
    }

    @Transactional
    public void registerUser(UserRegisterDto userRegisterDto,
                             Consumer<Authentication> successfulLoginProcessor) {

        checkIfExists(userRegisterDto);
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getRawPassword()));

        if (!this.dbExists()) {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.SUPER_ADMIN));
        } else {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.LIGHT));
        }

        this.userRepository.saveAndFlush(this.modelMapper.map(userRegisterDto, UserEntity.class));

        final UserDetails currentUser = appUserDetailsService.loadUserByUsername(userRegisterDto.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                currentUser, currentUser.getPassword(), currentUser.getAuthorities());

        successfulLoginProcessor.accept(auth);
    }

    private void checkIfExists(UserRegisterDto userRegisterDto) throws RuntimeException {
        Optional<UserEntity> userByUsername = userRepository.findByUsername(userRegisterDto.getUsername());
        Optional<UserEntity> userByEmail = userRepository.findByEmail(userRegisterDto.getEmail());

        boolean isSameUser = isSameUser(userByUsername, userByEmail);

        if (isSameUser) {
            throw new RuntimeException("Such user already exists. Please proceed to login");
        }

        if (userByEmail.isPresent()) {
            throw new RuntimeException("The email address is already registered with another username");
        }

        if (userByUsername.isPresent()) {
            throw new RuntimeException("The username is already taken");
        }
    }

    private boolean dbExists() {
        return userRepository.count() > 0;
    }

    private boolean isSameUser(Optional<UserEntity> user1, Optional<UserEntity> user2) {
        return user1.isPresent() &&
                user2.isPresent() &&
                user1.get().getId().equals(user2.get().getId());
    }

    public void checkAccessControl(Long ownerId, Long currentUserId) {
        if (!ownerId.equals(currentUserId)) {
            throw new AccessDeniedException("Access denied");
        }
    }

}
