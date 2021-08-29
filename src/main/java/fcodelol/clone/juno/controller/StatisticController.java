package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.dto.PeriodTime;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/statistic")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping(value = "/top/product/{number}")
    public List<ProductByGroupDto> getTopProducts(@PathVariable int number) {
        return statisticService.getTopProduct(number);
    }

    @GetMapping(value = "/top/customer/{number}")
    public List<UserByGroupDto> getTopCustomers(@PathVariable int number) {
        return statisticService.getTopCustomer(number);
    }
    @GetMapping(value = "/income")
    public List<BigDecimal> getIncomes(@RequestBody List<PeriodTime> periodTimes)
    {
        return statisticService.getIncomePerTime(periodTimes);
    }
}
