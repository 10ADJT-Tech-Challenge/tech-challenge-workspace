package br.com.fiap.tech.challenge.authservice.dto;

import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        List<String> roles
) {}