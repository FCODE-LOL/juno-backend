package fcodelol.clone.juno;

import fcodelol.clone.juno.controller.request.LoginRequest;
import fcodelol.clone.juno.controller.request.RegisterRequest;
import fcodelol.clone.juno.controller.request.SocialMediaLoginRequest;
import fcodelol.clone.juno.service.AuthenticationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {
    @Autowired
    AuthenticationService authenticationService;

    @Test
    public void register() {
        Assert.assertEquals("Register success", authenticationService.register(new RegisterRequest("trannhathoang8678@gmail.com"
                , "123456", authenticationService.sendTokenToEmail("trannhathoang8678@gmail.com"))));
    }

    @Test
    public void loginWithSocialMedia()
    {
        System.out.println(authenticationService.loginBySocialMedia(new SocialMediaLoginRequest("1234","Hoang")));
    }

    @Test
    public void login(){
        System.out.println(authenticationService.login(new LoginRequest("trannhathoang8678@gmail.com","123456")));
    }
}
