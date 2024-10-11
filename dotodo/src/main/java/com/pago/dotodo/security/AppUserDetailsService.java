package com.pago.dotodo.security;

import com.pago.dotodo.model.entity.UserEntity;
import com.pago.dotodo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findUserByUsernameOrEmail(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

    private UserDetails mapToUserDetails(UserEntity user) {
        return new CustomAuthUserDetails(
                user.getId(),
                user.getFirstName(),
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRole().name()))
        );
    }
}
