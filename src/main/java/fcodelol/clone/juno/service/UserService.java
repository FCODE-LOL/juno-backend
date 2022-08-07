package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.request.UpdatePasswordRequest;
import fcodelol.clone.juno.controller.request.UpdateUserInfoRequest;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private static final String BAN_USER_MESSAGE = "Ban user: ";
    private static final String USER_ID_NOT_EXIST_MESSAGE = "This user id is not exist";
    private static final String UNBAN_USER_MESSAGE = "Unban user: ";

    public List<UserByGroupExtendDto> getAllUser() {
        return userRepository.getAllUser().stream().map(user -> modelMapper.map(user, UserByGroupExtendDto.class)).collect(Collectors.toList());

    }

    public Response<UserDto> getUserById(int id) {
        return new Response<>(200, "Success", modelMapper.map(userRepository.getById(id), UserDto.class));
    }

    public Response<String> banUser(int id) {
        logger.info(BAN_USER_MESSAGE + id);
        User user = userRepository.findOneById(id);
        if (user == null) {
            logger.warn("{}{}",BAN_USER_MESSAGE,USER_ID_NOT_EXIST_MESSAGE);
            throw new CustomException(404, USER_ID_NOT_EXIST_MESSAGE);
        }
        if (user.getIsAdmin() != null && user.getIsAdmin()) {
            logger.warn("Ban user: You can not ban admin");
            throw new CustomException(403, "You can not ban admin");
        }
        user.setIsDisable(true);
        userRepository.save(user);
        logger.info("Ban user success");
        return new Response<>(200, "Ban user success");
    }

    public Response<String> unbanUser(int id) {
        logger.info(UNBAN_USER_MESSAGE + id);
        User user = userRepository.findOneById(id);
        if (user == null) {
            logger.warn("Unban user: This id is not exist");
            throw new CustomException(404, USER_ID_NOT_EXIST_MESSAGE);
        }
        user.setIsDisable(false);
        userRepository.save(user);
        return new Response<>(200, "Unban user success");
    }

    public Response<String> updatePassword(UpdatePasswordRequest updatePasswordRequest, Integer id) {
        logger.info("Change password:" + updatePasswordRequest);
        User user = userRepository.findOneById(id);
        if (!new BCryptPasswordEncoder().matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            logger.info("Password is not correct");
            throw new CustomException(400, "Password is not correct");
        }
        //set new password
        user.setPassword(new BCryptPasswordEncoder().encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        logger.info("Change password success");
        return new Response<>(200, "Change password success");
    }

    @Transactional
    public Response<String> updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest, Integer id) {
        logger.info("Update user info:" + updateUserInfoRequest);
        User user = userRepository.findOneById(id);
        user.updateInfo(updateUserInfoRequest);
        userRepository.save(user);
        logger.info("Change user info success");
        return new Response<>(200, "Change user info success");
    }
}

