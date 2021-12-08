package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.repository.UserRepository;
import fcodelol.clone.juno.repository.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(StatisticService.class);

    public List<UserByGroupExtendDto> getAllUser() {
        try {
            return userRepository.getAllUser().stream().map(user -> modelMapper.map(user, UserByGroupExtendDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Get all users error: " + e.getMessage());
            return null;
        }
    }

    public Response<UserDto> getUserById(int id) {
        try {
            return new Response(200, "Success", modelMapper.map(userRepository.getById(id), UserDto.class));
        } catch (Exception e) {
            logger.error("Get user by id: " + e.getMessage());
            return new Response(500, "Error");
        }
    }

    public Response banUser(int id) {
        try {
            User user = userRepository.findOneById(id);
            if (user == null)
                return new Response(404, "User is not exist");
            if (user.getIsAdmin() != null && user.getIsAdmin())
                return new Response(403, "You can not ban admin");
            user.setIsDisable(true);
            userRepository.save(user);
            return new Response(200, "Ban user success");
        } catch (Exception e) {
            logger.error("Ban user error:" + e.getMessage());
            return new Response(500, "Ban user failed");
        }
    }

    public Response unbanUser(int id) {
        try {
            User user = userRepository.findOneById(id);
            if (user == null)
                return new Response(404, "User is not exist");
            user.setIsDisable(false);
            userRepository.save(user);
            return new Response(200, "Unban user success");
        } catch (Exception e) {
            logger.error("Ban user error:" + e.getMessage());
            return new Response(500, "Unban user failed");
        }
    }
}

