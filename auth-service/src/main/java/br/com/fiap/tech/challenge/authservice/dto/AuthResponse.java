package br.com.fiap.tech.challenge.authservice.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {}