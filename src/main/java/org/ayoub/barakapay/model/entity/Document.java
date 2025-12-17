package org.ayoub.barakapay.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.ayoub.barakapay.enums.FileType;

import java.time.LocalDateTime;

@Entity
@Table(name = "dosuments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileType fileType;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;
}
