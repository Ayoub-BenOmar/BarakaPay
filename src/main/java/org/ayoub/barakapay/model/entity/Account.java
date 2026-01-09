package org.ayoub.barakapay.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 24)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false, unique = true)
    private User client;

    @OneToMany(mappedBy = "accountSource")
    private List<Operation> outgoingOperations;

    @OneToMany(mappedBy = "accountDestination")
    private List<Operation> incomingOperations;
}
