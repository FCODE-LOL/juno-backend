package fcodelol.clone.juno;

import fcodelol.clone.juno.service.ExecuteDBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ImportDBTest {
    @Autowired
    ExecuteDBService executeDBService;
    @Test
    public void loadData(){
        executeDBService.loadDBData();
    }
    @Test
    public void clearData(){
        executeDBService.clearDBData();
    }
}
