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
    private static final String LOGIN_MESSAGE = "Login: ";
    private static final String ACCOUNT_BANNED_MESSAGE = "This account is banned";
    private static final String EMAIL_NOT_FOUND_MESSAGE = "This email account is not existed";
    private static final String WRONG_PASSWORD_MESSAGE = "Wrong password";
    private static final String SOCIAL_MEDIA_LOGIN_MESSAGE = "Login by social media: ";
    private static final String REGISTER_MESSAGE = "Register: ";
    private static final String TOKEN_NOT_SENT_MESSAGE = "Validate token is not sent";
    private static final String RESEND_TOKEN_MESSAGE = "Please click sent token button"; // action when token is not sent
    private static final String EMAIL_REGISTERED_MESSAGE = "Email has already registered";
    private static final String WRONG_TOKEN_MESSAGE = "Wrong token, please check or send another token";
    private static final String REGISTER_SUCCESS_MESSAGE = "Register success";
    private static final String SEND_TOKEN_TO_EMAIL_MESSAGE = "Send token to email: ";
    private static final String SEND_TOKEN_EMAIL_TITTLE = "Token to verify your account in Juno website";
    private static final String RETRIEVE_PASSWORD_MESSAGE = "Retrieve password: ";
    private static final String EMAIL_NOT_REGISTER = "Email has not been register";
    public Response<String> login(LoginRequest loginRequest) throws NullPointerException{
        logger.info("{}{}",LOGIN_MESSAGE,loginRequest);
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null)
        {
            logger.warn("{}{}",LOGIN_MESSAGE,EMAIL_NOT_FOUND_MESSAGE);
            throw new CustomException(404,EMAIL_NOT_FOUND_MESSAGE);
        }
        if (Boolean.TRUE.equals(user.getIsDisable())) {
            logger.warn("{}{}",LOGIN_MESSAGE,ACCOUNT_BANNED_MESSAGE);
            throw new CustomException(403,ACCOUNT_BANNED_MESSAGE);
        }
        if (new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            String token = renderAuthenticationToken();
            user.setToken(token);
            //check suceess or not
            userRepository.save(user);
                logger.info("{}{}",LOGIN_MESSAGE,token);
                return new Response<>(200, token);
        }
        logger.warn("{}{}",LOGIN_MESSAGE,WRONG_PASSWORD_MESSAGE);
        throw new CustomException(401, WRONG_PASSWORD_MESSAGE);
    }

    public Response<String> loginBySocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        logger.info("{}{}",SOCIAL_MEDIA_LOGIN_MESSAGE,socialMediaLoginRequest);
        User user = userRepository.findBySocialMediaId(socialMediaLoginRequest.getSocialMediaId());
        if (user == null)
            user = registerForSocialMedia(socialMediaLoginRequest);
        else if (Boolean.TRUE.equals(user.getIsDisable())) {
            logger.warn("{}{}",SOCIAL_MEDIA_LOGIN_MESSAGE,ACCOUNT_BANNED_MESSAGE);
            throw new CustomException(403,ACCOUNT_BANNED_MESSAGE);
        }
        String token = renderAuthenticationToken();
        user.setToken(token);
        userRepository.save(user);
        logger.info("{}{}",SOCIAL_MEDIA_LOGIN_MESSAGE,token);
        return new Response<>(200, token);
    }

    private String renderAuthenticationToken() {
        return UUID.randomUUID().toString();
    }

    public Response<String> register(RegisterRequest registerUser) {
        logger.info("{}{}",REGISTER_MESSAGE,registerUser);
        User temporaryUser = userRepository.findByEmail(registerUser.getEmail());
        if (temporaryUser == null) {
            logger.warn("{}{}",REGISTER_MESSAGE,TOKEN_NOT_SENT_MESSAGE);
            throw new CustomException(401,REGISTER_MESSAGE + RESEND_TOKEN_MESSAGE);
        }
        if (Boolean.TRUE.equals(temporaryUser.getIsDisable())) {
            logger.warn("{}{}",REGISTER_MESSAGE,ACCOUNT_BANNED_MESSAGE);
            throw new CustomException(403, ACCOUNT_BANNED_MESSAGE);
        }
        if (temporaryUser.getPassword() != null) {
            logger.warn("{}{}",REGISTER_MESSAGE,EMAIL_REGISTERED_MESSAGE);
            throw new CustomException(100, EMAIL_REGISTERED_MESSAGE);
        }
        String sentToken = temporaryUser.getToken();
        if (!sentToken.equals(registerUser.getToken()))
        {
            logger.warn("{}{}",REGISTER_MESSAGE,WRONG_TOKEN_MESSAGE);
            return new Response<>(401, WRONG_TOKEN_MESSAGE);}
        temporaryUser.setPassword(new BCryptPasswordEncoder().encode(registerUser.getPassword())); // set password with Bcrypt encoding
        temporaryUser.setIsAdmin(false); // register is only for non-admin user
        userRepository.save(temporaryUser);
        logger.info(REGISTER_SUCCESS_MESSAGE);
        return new Response<>(200, REGISTER_SUCCESS_MESSAGE);
    }

    public User registerForSocialMedia(SocialMediaLoginRequest socialMediaLoginRequest) {
        User user = new User(socialMediaLoginRequest.getName(), socialMediaLoginRequest.getSocialMediaId());
        return userRepository.save(user);
    }

    public Response<String> sendTokenToEmail(String email) {
        logger.info("{}{}",SEND_TOKEN_TO_EMAIL_MESSAGE,email);
        User temporaryUser = userRepository.findByEmail(email);
        if (temporaryUser == null) {
            temporaryUser = new User();
            temporaryUser.setEmail(email);
        } else
            if (Boolean.TRUE.equals(temporaryUser.getIsDisable()))
            {
                logger.warn("{}{}",SEND_TOKEN_TO_EMAIL_MESSAGE,ACCOUNT_BANNED_MESSAGE);
                return new Response<>(403, ACCOUNT_BANNED_MESSAGE);
            }
        String token = generateRandomString(6);
        temporaryUser.setToken(token);
        sendEmailService.sendEmail(SEND_TOKEN_EMAIL_TITTLE, email, token);
        userRepository.save(temporaryUser);
        logger.info("{}{}",SEND_TOKEN_TO_EMAIL_MESSAGE, token);
        return new Response<>(200, token);
    }
    @Transactional
    public Response<String> retrievePassword(RetrievePasswordRequest retrievePasswordRequest) {
        logger.info("{}{}",RETRIEVE_PASSWORD_MESSAGE, retrievePasswordRequest);
        User temporaryUser = userRepository.findByEmail(retrievePasswordRequest.getEmail());
        String password;
        if ( temporaryUser.getPassword() == null)
        {
            logger.info("{}{}",RETRIEVE_PASSWORD_MESSAGE, EMAIL_NOT_REGISTER);
            throw new CustomException(400,EMAIL_NOT_REGISTER);
        }
        String sentToken = temporaryUser.getToken();
        if (!sentToken.equals(retrievePasswordRequest.getToken()))
        {
            logger.info("{}{}",RETRIEVE_PASSWORD_MESSAGE, WRONG_TOKEN_MESSAGE);
            throw new CustomException(400,WRONG_TOKEN_MESSAGE);
        }
        password = generateRandomString(6);
        temporaryUser.setPassword(new BCryptPasswordEncoder().encode(password));
        userRepository.save(temporaryUser);
        logger.info("{}{}",RETRIEVE_PASSWORD_MESSAGE, password);
        return new Response<>(200,RETRIEVE_PASSWORD_MESSAGE, password);
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
