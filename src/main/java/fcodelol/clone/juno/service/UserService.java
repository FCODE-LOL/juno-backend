package fcodelol.clone.juno.service;

import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.dto.UserDto;
import fcodelol.clone.juno.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(StatisticService.class);

    public List<UserByGroupExtendDto> getAllUser() {
        try {
            return (List<UserByGroupExtendDto>) userRepository.getALLUser();
        } catch (Exception e) {
            logger.error("Get all users error: " + e.getMessage());
            return null;
        }
    }

    public UserDto getUserById(int id) {
        try {
            return modelMapper.map(userRepository.getById(id), UserDto.class);
        } catch (Exception e) {
            logger.error("Get user by id: " + e.getMessage());
            return null;
        }
    }
}

