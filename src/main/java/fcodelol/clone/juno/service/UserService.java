package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.exception.CustomException;
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
        return userRepository.getAllUser().stream().map(user -> modelMapper.map(user, UserByGroupExtendDto.class)).collect(Collectors.toList());

    }

    public Response<UserDto> getUserById(int id) {
        return new Response(200, "Success", modelMapper.map(userRepository.getById(id), UserDto.class));
    }

    public Response banUser(int id) {
        logger.info("Ban user with id:" + id);
        User user = userRepository.findOneById(id);
        if (user == null) {
            logger.warn("Ban user: This id is not exist");
            throw new CustomException(404, "User is not exist");
        }
        if (user.getIsAdmin() != null && user.getIsAdmin()) {
            logger.warn("Ban user: You can not ban admin");
            throw new CustomException(403, "You can not ban admin");
        }
        user.setIsDisable(true);
        userRepository.save(user);
        logger.info("Ban user success");
        return new Response(200, "Ban user success");
    }

    public Response unbanUser(int id) {
        logger.info("Un");
        User user = userRepository.findOneById(id);
        if (user == null)
        {
            logger.warn("Unban user: This id is not exist");
            throw new CustomException(404, "User is not exist");
        }
        user.setIsDisable(false);
        userRepository.save(user);
        return new Response(200, "Unban user success");

    }
}

