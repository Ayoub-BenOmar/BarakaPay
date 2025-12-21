package org.ayoub.barakapay.service.impl;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.excepion.EmailExistsException;
import org.ayoub.barakapay.model.dto.RegisterUserDto;
import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.model.entity.User;
import org.ayoub.barakapay.model.mapper.UserMapper;
import org.ayoub.barakapay.repository.UserRepository;
import org.ayoub.barakapay.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(RegisterUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailExistsException("This email is already used: " + userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFullName(user.getFullName());
        user.setRole(Role.CLIENT);
        user.setActive(false);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto getById(Integer id) {
        User user = userRepository.findById(id).orElseThrow( () -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(UserDto user) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id).orElseThrow( () -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public void changeUserRole(Integer userId, Role role) {

    }

    public UserDto getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        return userMapper.toDto(user);
    }

    public User getCurrentUserEntity() {
        UserDto dto = getCurrentUserProfile();
        return userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
