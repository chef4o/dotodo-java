package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
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

    public UserDto getUserById(Long id) {
        return this.modelMapper.map(userRepository.findById(id), UserDto.class);
    }

    public boolean exists(UserDto userDto) {
        return this.userRepository.existsById(userDto.getId());
    }
}
