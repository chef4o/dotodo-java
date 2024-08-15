package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private List<UserEntity> getAllEntities() {
        return userRepository.findAll();
    }

    public List<UserDto> getAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userEntity -> modelMapper.map(userEntity, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public Optional<UserEntity> findUserByUsernameOrEmail(String username) {
        if (EmailValidator.getInstance().isValid(username)) {
            return userRepository.findByEmail(username);
        } else {
            return userRepository.findByUsername(username);
        }
    }

    public List<String> getUsersWithBirthday() {
        LocalDate today = LocalDate.now();

        return getAllEntities().stream()
                .filter(user -> {
                    LocalDate dateOfBirth = user.getDateOfBirth();
                    if (dateOfBirth != null) {
                        return dateOfBirth.getDayOfMonth() == today.getDayOfMonth()
                                && dateOfBirth.getMonthValue() == today.getMonthValue();
                    }
                    return false;
                })
                .map(UserEntity::getEmail)
                .collect(Collectors.toList());
    }
}
