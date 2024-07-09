package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserAuthDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAuthDto registerUser(UserRegisterDto userRegisterDto) {

        checkIfExists(userRegisterDto);

        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getRawPassword()));

        if (!this.dbExists()) {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.SUPER_ADMIN));
        } else {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.LIGHT));
        }

        final UserEntity userForInsert = this.modelMapper.map(userRegisterDto, UserEntity.class);

        return this.modelMapper
                .map(this.userRepository.saveAndFlush(userForInsert), UserAuthDto.class);
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
}
