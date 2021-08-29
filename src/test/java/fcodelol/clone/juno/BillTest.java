package fcodelol.clone.juno;

import fcodelol.clone.juno.service.BillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BillTest {
    @Autowired
    BillService billService;
    @Test
    public void setStatusBill(){
        System.out.println(billService.setBillStatus(1,2));
    }
    @Test
    public void deleteBill(){
        System.out.println(billService.deleteBill(1));
    }
}
