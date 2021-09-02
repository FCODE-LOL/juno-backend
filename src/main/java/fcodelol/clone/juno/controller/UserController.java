package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.service.AuthorizationService;
import fcodelol.clone.juno.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthorizationService authorizationService;
    @GetMapping
    public List<UserByGroupExtendDto> getAllUsers() {
        return userService.getAllUser();
    }
    @GetMapping(value = "/{id}")
    @Transactional
    public UserDto getUserById(@RequestHeader("Authorization") String token,@PathVariable int id) {
        if(authorizationService.getIdByToken(token)!=id && !authorizationService.getRoleByToken(token).equals("ADMIN"))
            return null;
        return userService.getUserById(id);
    }
    @PutMapping(value = "/{id}")
    public String banUser(int id){
        return userService.banUser(id);
    }
}