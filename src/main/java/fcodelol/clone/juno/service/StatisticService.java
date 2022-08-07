package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.request.PeriodTime;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.ProductRepository;
import fcodelol.clone.juno.repository.UserRepository;
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
    @Autowired
    ProductService productService;
    private static final String SUCCESS_MESSAGE = "Success";

    @Transactional
    public Response<List<BigDecimal>> getIncomePerTime(List<PeriodTime> periodTimes) {
        List<BigDecimal> incomes = new LinkedList<>();
        periodTimes.forEach(periodTime -> incomes.add(billRepository.getIncome(periodTime.getStartTime(), periodTime.getEndTime())));
        return new Response<>(200, SUCCESS_MESSAGE, incomes);
    }

    public Response<List<UserByGroupDto>> getTopCustomer(int numberOfCustomer) {
        return new Response<>(200, SUCCESS_MESSAGE, userRepository.getTopCustomer(PageRequest.of(0, numberOfCustomer)));
    }

    public Response<List<ProductByGroupDto>> getTopSaleProduct(int numberOfProduct) {
        return new Response<>(200, SUCCESS_MESSAGE,
                productService.setColorIdsListProductByGroupDto(
                        productRepository.getTopSaleProduct(numberOfProduct).stream().map(product -> new ProductByGroupDto((Object[]) product)).collect(Collectors.toList())));
    }

    public Response<List<ProductByGroupDto>> getTopIncomeProduct(int numberOfProduct) {
        return new Response<>(200, SUCCESS_MESSAGE,
                productService.setColorIdsListProductByGroupDto(
                        productRepository.getTopIncomeProduct(numberOfProduct).stream().map(product -> new ProductByGroupDto((Object[]) product)).collect(Collectors.toList())));
    }

    public Response<List<ProductByGroupDto>> getTopRelatedProduct(int productId, int numberOfProduct) {
        return new Response<>(200, SUCCESS_MESSAGE,
                productService.setColorIdsListProductByGroupDto(productRepository.getTopRelatedProduct(productId, numberOfProduct).stream()
                        .map(product -> new ProductByGroupDto((Object[]) product)).collect(Collectors.toList())));
    }
}