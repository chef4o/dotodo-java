package com.pago.dotodo.security;

import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.service.AuthService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AuthService authService;

    public AppUserDetailsService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authService.findUserByUsernameOrEmail(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    private UserDetails mapToUserDetails(UserEntity user) {
        return new CustomAuthUserDetails(
                user.getId(),
                user.getFirstName(),
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private List<GrantedAuthority> getAuthorities(UserEntity user) {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .collect(Collectors.toList());
    }
}
