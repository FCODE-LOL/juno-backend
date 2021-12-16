package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.request.LoginRequest;
import fcodelol.clone.juno.controller.request.RegisterRequest;
import fcodelol.clone.juno.controller.request.RetrievePasswordRequest;
import fcodelol.clone.juno.controller.request.SocialMediaLoginRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.repository.UserRepository;
import fcodelol.clone.juno.repository.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        logger.info("Login: " + loginRequest);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.getIsDisable()) {
            logger.warn("Login: This account is banned");
            throw new CustomException(403, "This account is banned");
        }
        if (user.getPassword() == null) {
            logger.warn("Login: This email account is not existed");
            throw new CustomException(404, "This email account is not existed");
        }
        if (new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            String token = renderAuthenticationToken();
            user.setToken(token);
            //check suceess or not
            if (userRepository.save(user) != null) {
                logger.info("Login success: " + token);
                return new Response(200, token);
            }
        }
        logger.warn("Login: Wrong password");
        throw new CustomException(401, "Wrong password");
    }

    public Response loginBySocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        logger.info("Login by social media: " + socialMediaLoginRequest);
        User user = userRepository.findBySocialMediaId(socialMediaLoginRequest.getSocialMediaId());
        if (user.getIsDisable()) {
            logger.warn("Login: This account is banned");
            throw new CustomException(403, "This account is banned");
        }
        String token = renderAuthenticationToken();
        if (user == null)
            user = registerForSocialMedia(socialMediaLoginRequest);
        user.setToken(token);
        userRepository.save(user);
        logger.info("Login by social media success:" + token);
        return new Response(200, token);
    }

    private String renderAuthenticationToken() {
        return UUID.randomUUID().toString();
    }

    public Response register(RegisterRequest registerUser) {
        logger.info("Register: " + registerUser);
        User temporaryUser = userRepository.findByEmail(registerUser.getEmail());
        if (temporaryUser == null) {
            logger.warn("Register: Validate token is not sent");
            throw new CustomException(401, "Please click sent token button");
        }
        if (temporaryUser.getIsDisable()) {
            logger.warn("Register: Account is banned");
            throw new CustomException(403, "This account is banned");
        }
        if (temporaryUser.getPassword() != null) {
            logger.warn("Register: Email has already registered");
            throw new CustomException(100, "Email has already registered");
        }
        String sentToken = temporaryUser.getToken();
        if (!sentToken.equals(registerUser.getToken()))
            return new Response(401, "Wrong token, please check or send another token");
        temporaryUser.setPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword()));
        temporaryUser.setIsAdmin(false);
        userRepository.save(temporaryUser);
        logger.info("Register success");
        return new Response(200, "Register success");
    }

    public User registerForSocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        User user = new User(socialMediaLoginRequest.getName(), socialMediaLoginRequest.getSocialMediaId());
        return userRepository.save(user);
    }

    public Response sendTokenToEmail(String email) {
        logger.info("Send token to email: " + email);
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
        logger.info("Sent token success: " + token);
        return new Response(200, token);
    }
    @Transactional
    public Response retrievePassword(RetrievePasswordRequest retrievePasswordRequest) {
        logger.info("Retrieve password:" + retrievePasswordRequest);
        User temporaryUser = userRepository.findByEmail(retrievePasswordRequest.getEmail());
        String password;
        if ( temporaryUser.getPassword() == null)
        {
            logger.info("Retrieve password: Email has not been registered" );
            throw new CustomException(400,"Email have not been registered");
        }
        String sentToken = temporaryUser.getToken();
        if (!sentToken.equals(retrievePasswordRequest.getToken()))
        {
            logger.info("Wrong token, please check or send another token");
            throw new CustomException(400,"Wrong token, please check or send another token");
        }
        password = generateRandomString(6);
        temporaryUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(temporaryUser);
        logger.info("Retrieve password success:" + password);
        return new Response(200,"Password: " + password);
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
