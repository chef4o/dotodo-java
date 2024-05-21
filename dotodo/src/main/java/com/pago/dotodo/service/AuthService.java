package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.model.entity.User;
import com.pago.dotodo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserTokenDto loggedUser;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthService(UserRepository userRepository, UserTokenDto loggedUser, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserDto registerUser(UserRegisterDto userRegisterDto) {
        UserDto currentUser = modelMapper.map(userRegisterDto, UserDto.class);
        String encodedPassword = passwordEncoder.encode(userRegisterDto.getPassword());
        currentUser.setPassword(encodedPassword);

        UserDto registeredUser = this.modelMapper
                .map(this.userRepository
                        .saveAndFlush(this.modelMapper.map(currentUser, User.class)), UserDto.class);

//        setLoggedUser(registeredUser);

        return registeredUser;
    }

//    public UserDto loginUser(UserLoginDto userLoginDto) {
//        UserDto current = getLoginUser(userLoginDto.getUsername());
//
//        setLoggedUser(current);
//
//        return current;
//    }

//
//    private void setLoggedUser(UserDto user) {
//        this.loggedUser
//                .setId(user.getId())
//                .setEmail(user.getEmail())
//                .setUsername(user.getUsername())
//                .setFirstName(user.getFirstName())
//                .setRole(user.getRole());
//    }

}
