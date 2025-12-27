package org.ayoub.barakapay.controller;

import lombok.RequiredArgsConstructor;
import org.ayoub.barakapay.model.dto.RegisterUserDto;
import org.ayoub.barakapay.model.dto.UserDto;
import org.ayoub.barakapay.service.impl.AccountServiceImpl;
import org.ayoub.barakapay.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;

    @GetMapping("/users")
    public List<UserDto> getAllUsers(){
        return userService.getAll();
    }

    @PutMapping("/activate/{id}")
    public UserDto activateUser(@PathVariable Integer id){
        return userService.activateUser(id);
    }

    @PostMapping("/add-client")
    public UserDto addClient(@RequestBody RegisterUserDto userDto){
        return userService.register(userDto);
    }
}
