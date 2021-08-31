package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.LoginRequest;
import fcodelol.clone.juno.controller.request.RegisterRequest;
import fcodelol.clone.juno.controller.request.SocialMediaLoginRequest;
import fcodelol.clone.juno.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PutMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest registerUser) {
        return authenticationService.register(registerUser);
    }

    @PostMapping("/social-media")
    public String loginWithSocialMedia(@RequestBody SocialMediaLoginRequest socialMediaLoginRequest) {
        return authenticationService.loginBySocialMedia(socialMediaLoginRequest);
    }
}
