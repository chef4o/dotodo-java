package com.pago.dotodo.service;

import com.pago.dotodo.model.dto.UserAuthDto;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final LayoutService layoutService;
    private String backgroundPage;

    @Autowired
    public AuthService(UserRepository userRepository,
                       ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, LayoutService layoutService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.layoutService = layoutService;
        this.backgroundPage = "home";
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

    public Optional<UserEntity> findUserByUsernameOrEmail(String username) {
        if (EmailValidator.getInstance().isValid(username)) {
            return userRepository.findByEmail(username);
        } else {
            return userRepository.findByUsername(username);
        }
    }

    public String getBackgroundPage() {
        return backgroundPage;
    }

    public void setBackgroundPage(HttpServletRequest request) {
        String lastPage = request.getHeader("Referer")
                .substring(request.getHeader("Referer").lastIndexOf("/") + 1);

        if (lastPage.isEmpty()) {
            this.backgroundPage = "home";
            return;
        }

        ArrayList<String> backgroundMenuPages = Stream
                .concat(layoutService.getMenuNames(layoutService.getSidebarNavItems()).stream(),
                        layoutService.getMenuNames(layoutService.getBottombarNavItems()).stream())
                .collect(Collectors.toCollection(ArrayList::new));

        if (backgroundMenuPages.contains(lastPage)) {
            this.backgroundPage = lastPage;
        }
    }
}
