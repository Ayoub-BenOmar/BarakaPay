package org.ayoub.barakapay.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ayoub.barakapay.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean isActive;

    @Column
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(mappedBy = "client", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Account account;
}
