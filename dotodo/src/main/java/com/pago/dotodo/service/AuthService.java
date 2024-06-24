package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        final UserDto userDto = modelMapper.map(userRegisterDto, UserDto.class)
                .setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        if (!this.dbExists()) {
            userDto.getRoles().add(String.valueOf(RoleEnum.SUPER_ADMIN));

        } else {
            userDto.getRoles().add(String.valueOf(RoleEnum.LIGHT));
        }

        final UserEntity userForInsert = this.modelMapper.map(userDto, UserEntity.class);

        return this.modelMapper
                .map(this.userRepository.saveAndFlush(userForInsert), UserDto.class);
    }

    private boolean dbExists() {
        return userRepository.count() > 0;
    }
}
