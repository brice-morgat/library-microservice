package com.example.library.userservice.dto;

import com.example.library.userservice.model.MembershipType;
import com.example.library.userservice.model.RoleName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String membershipNumber;
    private LocalDate membershipDate;
    private MembershipType membershipType;
    private boolean active;
    private Set<RoleName> roles;
}
