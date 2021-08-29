package fcodelol.clone.juno.service;

import fcodelol.clone.juno.dto.PrimaryUser;
import fcodelol.clone.juno.dto.RegisterUser;
import fcodelol.clone.juno.dto.SocialMediaUser;
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

    public String login(PrimaryUser primaryUser) {
        try {
            User user = userRepository.findByEmail(primaryUser.getEmail());
            if (user.getIsDisable())
                return "This account is banned";
            if (user.getPassword() == null)
                return "This email account is not existed";
            if (new BCryptPasswordEncoder().matches(primaryUser.getPassword(), user.getPassword())) {
                String token = renderAuthenticationToken();
                user.setToken(token);
                return userRepository.save(user) != null ? token : "Login failed, please log in again";
            }
            return "Wrong password";
        } catch (Exception e) {
            logger.error("Error in login with " + primaryUser.getEmail() + " password: " + primaryUser.getPassword() + "/n");
            return "Login failed";
        }
    }

    public String loginBySocialMedia(SocialMediaUser socialMediaUser) {
        try {
            User user = userRepository.findBySocialMediaId(socialMediaUser.getSocialMediaId());
            if (user.getIsDisable())
                return "This account is banned";
            String token = renderAuthenticationToken();
            if (user == null)
                user = registerForSocialMedia(socialMediaUser);
            user.setToken(token);
            userRepository.save(user);
            return token;
        } catch (Exception e) {
            logger.error("Error in login with social media " + socialMediaUser.getSocialMediaId() + " password: " + socialMediaUser.getName() + '\n' + e.getMessage() + '\n');
            return "Login failed";
        }
    }

    private String renderAuthenticationToken() {
        return UUID.randomUUID().toString();
    }

    public String register(RegisterUser registerUser) {
        try {
            User temporaryUser = userRepository.findByEmail(registerUser.getEmail());
            if (temporaryUser == null)
                return "Please click sent token button";
            if(temporaryUser.getIsDisable())
                return "This account is banned";
            if (temporaryUser.getPassword() != null)
                return "Email has already registered";
            String sentToken = temporaryUser.getToken();
            if (!sentToken.equals(registerUser.getToken()))
                return "Wrong token, please check or send another token ";
            temporaryUser.setPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword()));
            temporaryUser.setIsAdmin(false);
            userRepository.save(temporaryUser);
            return "Register success";
        } catch (Exception e) {
            logger.error("Error at register:" + e.getMessage());
            return "Register failed";
        }
    }

    public User registerForSocialMedia(SocialMediaUser socialMediaUser) {
        try {
            User user = new User(socialMediaUser.getName(), socialMediaUser.getSocialMediaId());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error at register for social media:" + e.getMessage());
            return null;
        }
    }

    public String sendTokenToEmail(String email) {
        try {
            User temporaryUser = userRepository.findByEmail(email);
            if(temporaryUser.getIsDisable())
                return "This account is banned";
            if (temporaryUser == null) {
                temporaryUser = new User();
                temporaryUser.setEmail(email);
            }
            String token = generateRandomString(6);
            temporaryUser.setToken(token);
            sendEmailService.sendEmail("Token to verify your account in Juno website", email, token);
            userRepository.save(temporaryUser);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "Send token failed";
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
