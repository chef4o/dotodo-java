package com.pago.dotodo.service;

import com.pago.dotodo.configuration.constraint.error.ExceptionErrors;
import com.pago.dotodo.model.dto.UserRegisterDto;
import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.model.error.EmailAlreadyExistsException;
import com.pago.dotodo.model.error.UserAlreadyExistsException;
import com.pago.dotodo.model.error.UsernameAlreadyExistsException;
import com.pago.dotodo.repository.UserRepository;
import com.pago.dotodo.security.AppUserDetailsService;
import com.pago.dotodo.security.CustomSecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AppUserDetailsService appUserDetailsService;
    private final SecurityContextRepository securityContextRepository;
    private final CustomSecurityConfig customSecurityConfig;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper,
                       AppUserDetailsService appUserDetailsService,
                       SecurityContextRepository securityContextRepository, CustomSecurityConfig customSecurityConfig) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailsService = appUserDetailsService;
        this.securityContextRepository = securityContextRepository;
        this.customSecurityConfig = customSecurityConfig;
    }

    @Transactional
    public void registerUser(UserRegisterDto userRegisterDto,
                             Consumer<Authentication> successfulLoginProcessor) {

        checkIfUserExists(userRegisterDto);
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getRawPassword()));
        assignUserRole(userRegisterDto);

        this.userRepository.saveAndFlush(this.modelMapper.map(userRegisterDto, UserEntity.class));

        final UserDetails currentUser = appUserDetailsService.loadUserByUsername(userRegisterDto.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                currentUser, currentUser.getPassword(), currentUser.getAuthorities());

        successfulLoginProcessor.accept(auth);
    }

    private void checkIfUserExists(UserRegisterDto userRegisterDto) throws RuntimeException {
        Optional<UserEntity> userByUsername = userRepository.findByUsername(userRegisterDto.getUsername());
        Optional<UserEntity> userByEmail = userRepository.findByEmail(userRegisterDto.getEmail());

        if (isSameUser(userByUsername, userByEmail)) {
            throw new UserAlreadyExistsException();
        }

        userByUsername.ifPresent(user -> {
            throw new UsernameAlreadyExistsException();
        });

        userByEmail.ifPresent(user -> {
            throw new EmailAlreadyExistsException();
        });
    }

    private void assignUserRole(UserRegisterDto userRegisterDto) {
        if (!this.dbExists()) {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.SUPER_ADMIN));
        } else {
            userRegisterDto.getRoles().add(String.valueOf(RoleEnum.LIGHT));
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

    public void validateOwnerAccess(Long ownerId, Long currentUserId) {
        if (!ownerId.equals(currentUserId)) {
            throw new AccessDeniedException(ExceptionErrors.NON_OWNER_ACCESS_DENIED);
        }
    }

    public void validateAdminAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                Arrays.stream(customSecurityConfig.getAdministrationRoles())
                        .noneMatch(role ->
                                authentication.getAuthorities()
                                        .contains(new SimpleGrantedAuthority("ROLE_" + role)))) {
            throw new AccessDeniedException(ExceptionErrors.NON_ADMIN_ACCESS_DENIED);
        }
    }

    public void initializeSecurityContext(Authentication authentication,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = strategy.createEmptyContext();
        context.setAuthentication(authentication);
        strategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

}
