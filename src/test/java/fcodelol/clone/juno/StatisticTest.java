package fcodelol.clone.juno;

import fcodelol.clone.juno.controller.request.PeriodTime;
import fcodelol.clone.juno.service.StatisticService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticTest {
    @Autowired
    StatisticService statisticService;
    @Test
    public void getProduct(){
        System.out.println(statisticService.getTopSaleProduct(1));
    }
    @Test
    public void getCustomer(){
        System.out.println(statisticService.getTopCustomer(1));
    }
    @Test
    public void getIncome(){
        List<PeriodTime> periodTimes = new LinkedList<>();
        periodTimes.add(new PeriodTime(Timestamp.valueOf("2020-08-25 08:56:12"),Timestamp.valueOf("2021-12-29 08:56:13")));
        System.out.println(statisticService.getIncomePerTime(periodTimes));
    }
}
