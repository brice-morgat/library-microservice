package com.example.library.userservice.mapper;

import com.example.library.userservice.dto.UserDto;
import com.example.library.userservice.model.Role;
import com.example.library.userservice.model.RoleName;
import com.example.library.userservice.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {
    private UserMapper() {}

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        Set<RoleName> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .membershipNumber(user.getMembershipNumber())
                .membershipDate(user.getMembershipDate())
                .membershipType(user.getMembershipType())
                .active(user.isActive())
                .roles(roles)
                .build();
    }
}
