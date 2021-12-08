package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.request.LoginRequest;
import fcodelol.clone.juno.controller.request.RegisterRequest;
import fcodelol.clone.juno.controller.request.SocialMediaLoginRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.repository.UserRepository;
import fcodelol.clone.juno.repository.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SendEmailService sendEmailService;
    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);
    private static final String DATA_FOR_RANDOM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static SecureRandom random = new SecureRandom();

    public Response login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user.getIsDisable())
                return new Response(403, "This account is banned");
            if (user.getPassword() == null)
                return new Response(404, "This email account is not existed");
            if (new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
                String token = renderAuthenticationToken();
                user.setToken(token);
                //check suceess or not
                if (userRepository.save(user) != null)
                    return new Response(200, token);
                else
                    return new Response(500, "Login failed, please log in again");
            }
            return new Response(401, "Wrong password");
        } catch (Exception e) {
            logger.error("Error in login with " + loginRequest.getEmail() + " password: " + loginRequest.getPassword() + "\n");
            return new Response(500, "Login failed");
        }
    }

    public Response loginBySocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        try {
            User user = userRepository.findBySocialMediaId(socialMediaLoginRequest.getSocialMediaId());
            if (user.getIsDisable())
                return new Response(401, "This account is banned");
            String token = renderAuthenticationToken();
            if (user == null)
                user = registerForSocialMedia(socialMediaLoginRequest);
            user.setToken(token);
            userRepository.save(user);
            return new Response(200, "success");
        } catch (Exception e) {
            logger.error("Error in login with social media " + socialMediaLoginRequest.getSocialMediaId() + " password: " + socialMediaLoginRequest.getName() + '\n' + e.getMessage() + '\n');
            return new Response(500, "Login failed");
        }
    }

    private String renderAuthenticationToken() {
        return UUID.randomUUID().toString();
    }

    public Response register(RegisterRequest registerUser) {
        try {
            User temporaryUser = userRepository.findByEmail(registerUser.getEmail());
            if (temporaryUser == null)
                return new Response(401, "Please click sent token button");
            if (temporaryUser.getIsDisable())
                return new Response(403, "This account is banned");
            if (temporaryUser.getPassword() != null)
                return new Response(100, "Email has already registered");
            String sentToken = temporaryUser.getToken();
            if (!sentToken.equals(registerUser.getToken()))
                return new Response(401, "Wrong token, please check or send another token");
            temporaryUser.setPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword()));
            temporaryUser.setIsAdmin(false);
            userRepository.save(temporaryUser);
            return new Response(200, "Register success");
        } catch (Exception e) {
            logger.error("Error at register:" + e.getMessage());
            return new Response(500, "Register failed");
        }
    }

    public User registerForSocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        try {
            User user = new User(socialMediaLoginRequest.getName(), socialMediaLoginRequest.getSocialMediaId());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error at register for social media:" + e.getMessage());
            return null;
        }
    }

    public Response sendTokenToEmail(String email) {
        try {
            User temporaryUser = userRepository.findByEmail(email);
            if (temporaryUser == null) {
                temporaryUser = new User();
                temporaryUser.setEmail(email);
            } else {
                if (temporaryUser.getIsDisable())
                    return new Response(403, "This account is banned");
            }
            String token = generateRandomString(6);
            temporaryUser.setToken(token);
            sendEmailService.sendEmail("Token to verify your account in Juno website", email, token);
            userRepository.save(temporaryUser);
            return new Response(200, token);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response(500, "Send token failed");
        }
    }

    public String generateRandomString(int length) {
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }
}
