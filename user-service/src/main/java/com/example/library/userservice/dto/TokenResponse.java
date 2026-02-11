package com.example.library.userservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TokenResponse {
    private String token;
    private String tokenType;
    private Instant expiresAt;
}
