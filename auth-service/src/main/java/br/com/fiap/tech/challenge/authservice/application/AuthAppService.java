package br.com.fiap.tech.challenge.authservice.application;

import br.com.fiap.tech.challenge.authservice.domain.UserRole;
import br.com.fiap.tech.challenge.authservice.dto.AuthResponse;
import br.com.fiap.tech.challenge.authservice.dto.LoginRequest;
import br.com.fiap.tech.challenge.authservice.dto.RegisterRequest;
import br.com.fiap.tech.challenge.authservice.dto.UserResponse;
import br.com.fiap.tech.challenge.authservice.infrastructure.persistence.UserEntity;
import br.com.fiap.tech.challenge.authservice.infrastructure.persistence.UserRepository;
import br.com.fiap.tech.challenge.authservice.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthAppService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String bootstrapSecret;

    public AuthAppService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${app.admin.bootstrap-secret}") String bootstrapSecret
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.bootstrapSecret = bootstrapSecret;
    }

    public UserResponse registerClient(RegisterRequest req) {
        return register(req, UserRole.CLIENTE);
    }

    public UserResponse registerAdmin(String adminSecret, RegisterRequest req) {
        if (adminSecret == null || adminSecret.isBlank() || !adminSecret.equals(bootstrapSecret)) {
            throw new IllegalArgumentException("Admin secret inválido.");
        }
        return register(req, UserRole.ADMIN);
    }

    private UserResponse register(RegisterRequest req, UserRole role) {
        String email = req.email().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        UserEntity u = new UserEntity();
        u.setName(req.name());
        u.setEmail(email);
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        u.setRole(role);

        UserEntity saved = userRepository.save(u);

        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail(),
                List.of("ROLE_" + saved.getRole().name()));
    }

    public AuthResponse login(LoginRequest req) {
        UserEntity u = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas."));

        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciais inválidas.");
        }

        String token = jwtService.generateToken(u.getId(), u.getEmail(), u.getRole());
        return new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }
}