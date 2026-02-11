package com.example.library.loanservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private boolean active;
}
