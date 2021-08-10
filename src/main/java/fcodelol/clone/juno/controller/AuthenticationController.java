package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.dto.PrimaryUser;
import fcodelol.clone.juno.dto.RegisterUser;
import fcodelol.clone.juno.dto.SocialMediaUser;
import fcodelol.clone.juno.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PutMapping("/login")
    public String login(@RequestBody PrimaryUser primaryUser) {
        return authenticationService.login(primaryUser);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterUser registerUser) {
        return authenticationService.register(registerUser);
    }

    @PostMapping("/social-media")
    public String loginWithSocialMedia(@RequestBody SocialMediaUser socialMediaUser) {
        return authenticationService.loginBySocialMedia(socialMediaUser);
    }
}
