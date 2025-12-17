package org.ayoub.barakapay.model.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private Integer id;
    private String email;
    private String password;
}
