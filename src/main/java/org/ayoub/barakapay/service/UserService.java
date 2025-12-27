package org.ayoub.barakapay.service;

import org.ayoub.barakapay.enums.Role;
import org.ayoub.barakapay.model.dto.RegisterUserDto;
import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.model.entity.User;

import java.util.List;

public interface UserService {

    UserDto register(RegisterUserDto user);
    UserDto getById(Integer id);
    List<UserDto> getAll();
    UserDto activateUser(Integer userId);
    UserDto update(UserDto user);
    void delete(Integer id);
    void changeUserRole(Integer userId, Role role);
}
