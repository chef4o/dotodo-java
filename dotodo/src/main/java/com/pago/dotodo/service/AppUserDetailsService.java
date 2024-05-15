package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AppUserDetailsService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AppUserDetailsService(ModelMapper modelMapper, UserRepository userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  modelMapper.map(getLoginUser(username), UserDetails.class);
    }

    public UserDto getLoginUser(String username) {
        if (EmailValidator.getInstance().isValid(username)) {
            return this.modelMapper
                    .map(userRepository.findByEmail(username), UserDto.class);
        }
        return this.modelMapper
                .map(userRepository.findByUsername(username), UserDto.class);
    }
}
