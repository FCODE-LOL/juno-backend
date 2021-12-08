package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.PeriodTime;
import fcodelol.clone.juno.controller.response.Response;
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

    @GetMapping(value = "/top/product/sale/{quantity}")
    public Response<List<ProductByGroupDto>> getTopSaleProducts(@PathVariable int quantity) {
        return statisticService.getTopSaleProduct(quantity);
    }

    @GetMapping(value = "/top/product/income/{quantity}")
    public Response<List<ProductByGroupDto>> getTopIncomeProducts(@PathVariable int quantity) {
        return statisticService.getTopIncomeProduct(quantity);
    }
    @GetMapping(value = "/top/product/relate/{productId}&{quantity}")
    public Response<List<ProductByGroupDto>> getTopRelatedProducts(@PathVariable int productId,@PathVariable int quantity) {
        return statisticService.getTopRelatedProduct(productId,quantity);
    }
    @GetMapping(value = "/top/customer/{quantity}")
    public Response<List<UserByGroupDto>> getTopCustomers(@PathVariable int quantity) {
        return statisticService.getTopCustomer(quantity);
    }
    @GetMapping(value = "/income")
    public Response<List<BigDecimal>> getIncomes(@RequestBody List<PeriodTime> periodTimes)
    {
        return statisticService.getIncomePerTime(periodTimes);
    }
}
