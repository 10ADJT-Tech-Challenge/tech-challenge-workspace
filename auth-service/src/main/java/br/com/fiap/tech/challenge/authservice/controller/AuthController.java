package br.com.fiap.tech.challenge.authservice.controller;

import br.com.fiap.tech.challenge.authservice.application.AuthAppService;
import br.com.fiap.tech.challenge.authservice.domain.UserRole;
import br.com.fiap.tech.challenge.authservice.dto.AuthResponse;
import br.com.fiap.tech.challenge.authservice.dto.LoginRequest;
import br.com.fiap.tech.challenge.authservice.dto.RegisterRequest;
import br.com.fiap.tech.challenge.authservice.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthAppService authAppService;

    public AuthController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        return authAppService.registerClient(request);
    }

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerAdmin(
            @RequestHeader(name = "X-ADMIN-SECRET") String adminSecret,
            @RequestBody @Valid RegisterRequest request
    ) {
        return authAppService.registerAdmin(adminSecret, request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authAppService.login(request);
    }
}