package fcodelol.clone.juno;

import fcodelol.clone.juno.dto.UserByGroupExtendDto;
import fcodelol.clone.juno.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    UserService userService;
    @Test
    public void getAllUserTest(){
        for(UserByGroupExtendDto user : userService.getAllUser())
            System.out.println(user);
    }
}
