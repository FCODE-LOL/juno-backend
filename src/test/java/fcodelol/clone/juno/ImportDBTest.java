package fcodelol.clone.juno;

import fcodelol.clone.juno.service.ImportDBService;
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
    ImportDBService importDBService;
    @Test
    public void loadData(){
        importDBService.loadDBData();
    }
    @Test
    public void clearData(){
        importDBService.clearDBData();
    }
}
