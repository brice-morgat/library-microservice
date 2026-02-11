package com.example.library.userservice.security;

import com.example.library.userservice.model.RoleName;
import lombok.Getter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class UserPrincipal {
    private final Long id;
    private final String email;
    private final Set<RoleName> roles;

    public UserPrincipal(Long id, String email, Set<RoleName> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
