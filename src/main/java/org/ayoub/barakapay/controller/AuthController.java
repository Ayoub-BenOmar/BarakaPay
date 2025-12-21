package org.ayoub.barakapay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.excepion.EmailExistsException;
import org.ayoub.barakapay.model.dto.RegisterUserDto;
import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.service.UserService;
import org.ayoub.barakapay.service.security.CustomAuthenticationProvider;
import org.ayoub.barakapay.service.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            UserDto created = userService.register(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody RegisterUserDto registerUserDto) {
        Authentication authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(registerUserDto.getEmail(), registerUserDto.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(registerUserDto.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }
}
