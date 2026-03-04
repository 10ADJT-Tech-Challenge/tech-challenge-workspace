package br.com.fiap.tech.challenge.authservice.infrastructure.persistence;

import br.com.fiap.tech.challenge.authservice.domain.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable=false, length=120)
    private String name;

    @Column(nullable=false, length=180, unique=true)
    private String email;

    @Column(name="password_hash", nullable=false, length=120)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=30)
    private UserRole role;

    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}