package com.example.library.userservice.dto;

import com.example.library.userservice.model.MembershipType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private MembershipType membershipType;

    private boolean active = true;

    private Set<String> roles;
}
