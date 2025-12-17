package org.ayoub.barakapay.model.dto;

import lombok.Data;
import org.ayoub.barakapay.enums.Role;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Integer id;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
    private Integer accountId;
}

