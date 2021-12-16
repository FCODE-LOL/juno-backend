package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.UpdatePasswordRequest;
import fcodelol.clone.juno.controller.request.UpdateUserInfoRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.exception.CustomException;
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
        return new Response(200, "Success", userService.getAllUser());
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public Response<UserDto> getUserById(@RequestHeader("Authorization") String token, @PathVariable int id) {
        if (authorizationService.getUserIdByToken(token) != id && !authorizationService.getRoleByToken(token).equals("ADMIN")) {
            throw new CustomException(403, "You don't have permission for this API");
        }
        return userService.getUserById(id);
    }

    @PutMapping(value = "/ban/{id}")
    public Response banUser(@PathVariable int id) {
        return userService.banUser(id);
    }

    @PutMapping(value = "/unban/{id}")
    public Response unbanUser(@PathVariable int id) {
        return userService.unbanUser(id);
    }

    @PutMapping(value = "/update/password")
    public Response updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return userService.updatePassword(updatePasswordRequest);
    }

    @PutMapping(value = "/update/info")
    public Response updateInfo(@RequestHeader("Authorization") String token, @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        Integer id = authorizationService.getUserIdByToken(token);
        if (id == null && !authorizationService.getRoleByToken(token).equals("ADMIN")) {
            throw new CustomException(403, "You don't have permission for this API");
        }
        return userService.updateUserInfo(updateUserInfoRequest,id);
    }
}