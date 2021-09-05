package fcodelol.clone.juno.service;

import fcodelol.clone.juno.dto.PeriodTime;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.ProductRepository;
import fcodelol.clone.juno.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    private static final Logger logger = LogManager.getLogger(StatisticService.class);
    @Autowired
    ModelMapper modelMapper;

    @Transactional
    public List<BigDecimal> getIncomePerTime(List<PeriodTime> periodTimes) {
        try {
            List<BigDecimal> incomes = new LinkedList<>();
            periodTimes.forEach(periodTime -> incomes.add(billRepository.getIncome(periodTime.getStartTime(), periodTime.getEndTime())));
            return incomes;
        } catch (Exception e) {
            logger.error("Get income error" + e.getMessage());
            return null;
        }
    }

    public List<UserByGroupDto> getTopCustomer(int numberOfCustomer) {
        try {
            return userRepository.getTopCustomer(PageRequest.of(0,numberOfCustomer)).stream().map(customer -> modelMapper.map(customer,UserByGroupDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error get top customer:" + e.getMessage());
            return null;
        }
    }

    public List<ProductByGroupDto> getTopProduct(int numberOfProduct) {
        try {
            return  productRepository.getTopProduct(numberOfProduct).stream().map(product -> new ProductByGroupDto((Object[]) product)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Get top student:" + e.getMessage());
            return null;
        }
    }
}