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
import org.ayoub.barakapay.service.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
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
    private final AccountServiceImpl accountService;

    @Override
    public UserDto register(RegisterUserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailExistsException("This email is already used: " + userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFullName(user.getFullName());
        if (user.getRole() == null){
            user.setRole(Role.ROLE_CLIENT);
        }

        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();

        if (authUser != null && authUser.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            User currentUser = customUserDetails.getUser();

            if (currentUser.getRole() == Role.ROLE_ADMIN) {
                user.setActive(true);
            } else {
                user.setActive(false);
            }
        } else {
            user.setActive(false);
        }

        User savedUser = userRepository.save(user);

        user.setAccount(accountService.addAccount(user));
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
    public UserDto activateUser(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("User not found with id: " + userId));
        user.setActive(true);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
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
    public UserDto changeUserRole(Integer userId, Role role) {
        User user = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("User not found with id: " + userId));
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toDto(user);
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
