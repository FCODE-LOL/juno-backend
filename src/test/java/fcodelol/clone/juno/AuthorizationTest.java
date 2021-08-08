package fcodelol.clone.juno;

import fcodelol.clone.juno.service.AuthorizationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTest {
    @Autowired
    AuthorizationService authorizationService;
    @Test
    public void getUserByRole(){
        Assert.assertEquals("a",authorizationService.getRoleByToken("ABC"));
    }
}
