package fcodelol.clone.juno.service;

import fcodelol.clone.juno.interceptor.GatewayConstant;
import fcodelol.clone.juno.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;

import java.sql.Timestamp;


@Service
public class AuthorizationService {
    private static final Logger logger = LogManager.getLogger(AuthorizationService.class);
    @Autowired
    UserRepository userRepository;
    public String getRoleByToken(String token)
    {
        try{
            System.out.println(System.currentTimeMillis() + "/n" +
            (System.currentTimeMillis() + GatewayConstant.validAuthenticationTime));
            Boolean isAdmin =userRepository.findRoleByToken(token,new Timestamp(System.currentTimeMillis() + GatewayConstant.validAuthenticationTime));
            if(isAdmin == null)
                return "GUESS";
            return isAdmin.booleanValue() ? "ADMIN" : "MEMBER";
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
    public int getUserIdByToken(String token)
    {
        try {
            return userRepository.getIdByToken(token);
        }
        catch (Exception e){
            logger.error("Get id by token" + e.getMessage());
            return 0;
        }
    }
}
