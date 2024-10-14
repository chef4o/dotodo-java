package com.pago.dotodo.user.service;

import com.pago.dotodo.common.security.CustomAuthUserDetails;
import com.pago.dotodo.common.service.CloudService;
import com.pago.dotodo.user.model.dto.AdminPanelUserDto;
import com.pago.dotodo.user.model.dto.EditUserProfile;
import com.pago.dotodo.user.model.dto.UserDto;
import com.pago.dotodo.user.model.dto.UserProfileView;
import com.pago.dotodo.user.model.entity.AddressEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import com.pago.dotodo.user.model.enums.RoleEnum;
import com.pago.dotodo.user.repository.AddressRepository;
import com.pago.dotodo.user.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;
    private final CloudService cloudService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper,
                       AddressRepository addressRepository, CloudService cloudService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.cloudService = cloudService;
    }

    public UserProfileView getProfileDetails(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, UserProfileView.class)
                .setFullName();
    }

    public EditUserProfile getEditProfileDetails(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.map(userEntity, EditUserProfile.class);
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
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with id: " + id + " not found"));
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
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

    public List<AdminPanelUserDto> getLowerLevelUsers(Long id) {
        RoleEnum currentUserRole = getUserById(id)
                .getRole()
                .getRole();

        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getRole().getRole().ordinal() < currentUserRole.ordinal())
                .map(userEntity -> modelMapper.map(userEntity, AdminPanelUserDto.class))
                .collect(Collectors.toList());
    }

    public void editUserDetails(EditUserProfile profileEditDetails, CustomAuthUserDetails userDetails) {
        UserEntity user = userRepository
                .findById(userDetails.getId())
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

        if (profileEditDetails.getDob() != null && !profileEditDetails.getDob().isBlank()) {
            user.setDateOfBirth(modelMapper.map(profileEditDetails.getDob(), LocalDate.class));
        } else if (user.getDateOfBirth() != null && profileEditDetails.getDob().isEmpty()) {
            user.setDateOfBirth(null);
        }

        if (!Objects.equals(user.getPhoneNumber(), profileEditDetails.getPhoneNumber())) {
            user.setPhoneNumber(profileEditDetails.getPhoneNumber());
        }

        if (user.getAddress() != null) {
            if (user.getAddress().getStreet() == null ||
                    !user.getAddress().getStreet().equals(profileEditDetails.getStreet())) {
                user.getAddress().setStreet(profileEditDetails.getStreet());
            }

            if (user.getAddress().getTown() == null ||
                    !user.getAddress().getTown().equals(profileEditDetails.getTown())) {
                user.getAddress().setTown(profileEditDetails.getTown());
            }
        } else if (user.getAddress() == null) {
            AddressEntity address = new AddressEntity()
                    .setStreet(profileEditDetails.getStreet())
                    .setTown(profileEditDetails.getTown());
            addressRepository.saveAndFlush(address);
            user.setAddress(address);
        }

        if (profileEditDetails.getProfilePicture() != null && !profileEditDetails.getProfilePicture().isEmpty()) {
            user.setImageUrl(cloudService.saveImage(profileEditDetails.getProfilePicture()));
        }

        userRepository.saveAndFlush(user);
    }

    public boolean existsOnOtherAccount(String field, String parameter, Long targetUser) {

        UserDto targetUserDto = modelMapper.map(getUserById(targetUser), UserDto.class);

        if (field.equals("email")) {
            UserDto emailUser = modelMapper.map(getUserByEmail(parameter), UserDto.class);
            return emailUser != null && !emailUser.getEmail().equals(targetUserDto.getEmail());
        }

        if (field.equals("username")) {
            UserDto usernameUser = modelMapper.map(getUserByUsername(parameter), UserDto.class);
            return usernameUser != null && !usernameUser.getUsername().equals(targetUserDto.getUsername());
        }
        throw new RuntimeException("Cannot decide on method return result");
    }

    public boolean dateOfBirthMismatch(EditUserProfile editedUser, Long loggedUserId) {
        LocalDate loggedUsedDob = getUserById(loggedUserId).getDateOfBirth();

        if (loggedUsedDob != null) {
            return editedUser.getDob() != null && !editedUser.getDob().isEmpty()
                    && !loggedUsedDob.equals(modelMapper.map(editedUser.getDob(), LocalDate.class));
        }
        return false;
    }

}
