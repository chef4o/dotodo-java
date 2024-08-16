package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserDto;
import com.pago.dotodo.model.entity.AddressEntity;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.view.UserProfileView;
import com.pago.dotodo.repository.AddressRepository;
import com.pago.dotodo.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
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

    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
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

    public void editUserDetails(UserProfileView profileEditDetails) {
        UserEntity user = userRepository
                .findByEmail(profileEditDetails.getEmail())
                .orElseThrow();

        if (!Objects.equals(user.getFirstName(), profileEditDetails.getFirstName())) {
            user.setFirstName(profileEditDetails.getFirstName());
        }

        if (!Objects.equals(user.getLastName(), profileEditDetails.getLastName())) {
            user.setLastName(profileEditDetails.getLastName());
        }

        if (!Objects.equals(user.getUsername(), profileEditDetails.getUsername())) {
            user.setUsername(profileEditDetails.getUsername());
        }

        if (!Objects.equals(user.getEmail(), profileEditDetails.getEmail())) {
            user.setEmail(profileEditDetails.getEmail());
        }

        if (user.getDateOfBirth() == null && profileEditDetails.getDob() != null) {
            user.setDateOfBirth(modelMapper.map(profileEditDetails.getDob(), LocalDate.class));
        } else if (user.getDateOfBirth() != null && profileEditDetails.getDob() != null) {
            throw new RuntimeException("Date of birth cannot be edited");
        }

        if (!Objects.equals(user.getImageUrl(), profileEditDetails.getImgUrl())) {
            user.setImageUrl(profileEditDetails.getImgUrl());
        }

        if (!Objects.equals(user.getPhoneNumber(), profileEditDetails.getPhoneNumber())) {
            user.setPhoneNumber(profileEditDetails.getPhoneNumber());
        }

        if (user.getAddress() != null && !Objects.equals(user.getAddress().toString(), profileEditDetails.getAddress())) {
            user.getAddress().setStreet(profileEditDetails.getAddress());
        } else if (user.getAddress() == null) {
            AddressEntity address = new AddressEntity().setStreet(profileEditDetails.getAddress());
            addressRepository.saveAndFlush(address);
            user.setAddress(address);
        }

        userRepository.saveAndFlush(user);
    }

    public boolean existsOnOtherAccount(String field, UserProfileView editedUser, Long loggedUserId) {

        UserDto loggedUserDto = modelMapper.map(getUserById(loggedUserId), UserDto.class);
        UserDto editedUserDto = modelMapper.map(editedUser, UserDto.class);

        if (field.equals("email")) {
            UserDto emailUser = modelMapper.map(getUserByEmail(editedUserDto.getEmail()), UserDto.class);
            return !emailUser.getEmail().equals(loggedUserDto.getEmail());
        }

        if (field.equals("username")) {
            UserDto usernameUser = modelMapper.map(getUserByUsername(editedUserDto.getUsername()), UserDto.class);
            return usernameUser != null && !usernameUser.getUsername().equals(loggedUserDto.getUsername());
        }
        throw new RuntimeException("Cannot decide on method return result");
    }

}
