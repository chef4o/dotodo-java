package com.pago.dotodo.service;

import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserProfileView getProfileDetails(Long id) {
        return this.modelMapper
                .map(userRepository.findById(id), UserProfileView.class)
                .setFullName();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
