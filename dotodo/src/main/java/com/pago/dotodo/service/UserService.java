package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.dto.binding.UserTokenDto;
import com.pago.dotodo.model.entity.User;
import com.pago.dotodo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserTokenDto loggedUser;

    @Autowired
    public UserService(UserRepository userRepository, UserTokenDto loggedUser) {
        this.userRepository = userRepository;
        this.loggedUser = loggedUser;
    }

    private UserTokenDto mapToToken(User user) {
        return new UserTokenDto()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setEmail(user.getEmail())
                .setNickname(user.getUsername());
    }

    private UserDto mapToDto(User user) {
        String address = user.getAddress().getStreet() + ", " +
                user.getAddress().getTown().getName() + " " +
                user.getAddress().getZipCode() + ", " +
                user.getAddress().getTown().getCountry().getName();

        return new UserDto()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setNickname(user.getUsername())
                .setEmail(user.getEmail())
                .setDob(user.getDateOfBirth())
                .setRole(user.getRole())
                .setAvatarId(user.getImageUrl())
                .setAddress(address);
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
