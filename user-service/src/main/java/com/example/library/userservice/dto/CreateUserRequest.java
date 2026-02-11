package com.example.library.userservice.dto;

import com.example.library.userservice.model.MembershipType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String membershipNumber;

    @NotNull
    private MembershipType membershipType;

    private boolean active = true;

    private Set<String> roles;
}
