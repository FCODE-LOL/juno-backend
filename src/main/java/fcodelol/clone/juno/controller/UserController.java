package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.response.Response;
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
    public Response<List<UserByGroupExtendDto>> getAllUsers() {
        return new Response(200,"Success",userService.getAllUser());
    }
    @GetMapping(value = "/{id}")
    @Transactional
    public Response<UserDto> getUserById(@RequestHeader("Authorization") String token,@PathVariable int id) {
        if(authorizationService.getUserIdByToken(token)!=id && !authorizationService.getRoleByToken(token).equals("ADMIN"))
            return null;
        return userService.getUserById(id);
    }
    @PutMapping(value = "/ban/{id}")
    public Response banUser(@PathVariable int id){
        return userService.banUser(id);
    }
    @PutMapping(value = "/unban/{id}")
    public Response unbanUser(@PathVariable int id){
        return userService.unbanUser(id);
    }
}