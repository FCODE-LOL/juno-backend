package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public List<UserByGroupExtendDto> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(int id) {
        return userService.getUserById(id);
    }
}