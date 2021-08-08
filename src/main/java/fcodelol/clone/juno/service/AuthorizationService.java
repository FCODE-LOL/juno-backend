package fcodelol.clone.juno.service;

import fcodelol.clone.juno.repository.UserRepository;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;


@Service
public class AuthorizationService {
    private static final Logger logger = LogManager.getLogger(AuthorizationService.class);
    @Autowired
    UserRepository userRepository;
    public String getRoleByToken(String token)
    {
        try{
            return userRepository.findRoleByToken(token);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }
}
