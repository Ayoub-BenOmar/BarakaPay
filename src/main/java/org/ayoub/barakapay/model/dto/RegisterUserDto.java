package org.ayoub.barakapay.model.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String fullName;
    private String password;
}
