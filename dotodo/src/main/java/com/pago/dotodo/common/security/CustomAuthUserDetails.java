package com.pago.dotodo.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomAuthUserDetails extends User {

    private final Long id;
    private final String firstName;

    public CustomAuthUserDetails(Long id, String firstName, String username, String password,
                                 Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.firstName = firstName;
    }

    public CustomAuthUserDetails(Long id, String firstName, String username, String password,
                                 boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                                 boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Long getId() {
        return id;
    }
}
