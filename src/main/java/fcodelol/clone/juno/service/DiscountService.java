package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.controller.response.DiscountModelResponse;
import fcodelol.clone.juno.controller.response.DiscountResponse;
import fcodelol.clone.juno.dto.*;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.repository.DiscountModelRepository;
import fcodelol.clone.juno.repository.DiscountRepository;
import fcodelol.clone.juno.repository.ModelRepository;
import fcodelol.clone.juno.repository.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    DiscountModelRepository discountModelRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    ModelMapper modelMapper;

    private ThreadPoolTaskScheduler taskScheduler;
    private HashMap<Integer, Future> futureMap;

    private static final Logger logger = LogManager.getLogger(DiscountService.class);

    public DiscountService() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        taskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        taskScheduler.initialize();
        futureMap = new HashMap<>();
    }

    @Transactional
    public void clearScheduler() {
        for (Future future : futureMap.values())
            future.cancel(false);
        futureMap.clear();
    }

    @Transactional
    public void restartAllDiscountEvent() throws Exception {
        logger.info("Start restart all discount event");
        List<Discount> discountList = discountRepository.findByIsDisable(false);
        if (discountList == null) return;
        futureMap.clear();
        for (Discount discount : discountList)
            if (discount.getCode() == null) //discount without code
            {
                futureMap.put(discount.getId() * 2, taskScheduler.schedule(runStartEvent(discount.getStartTime(), discount.getId())
                        , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
                futureMap.put(discount.getId() * 2 + 1, taskScheduler.schedule(runFinishEvent(discount.getStartTime(), discount.getId())
                        , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
            }
        logger.info("Restart discount events success");
    }

    @Transactional
    public Response addDiscountEvent(DiscountDto discountDto) {
        logger.info("Add discount: " + discountDto);
        if (!validDiscountTime(discountDto.getStartTime(), discountDto.getEndTime())) {
            logger.warn("Start and end time is not suitable");
            throw new CustomException(400, "Start and end time is not suitable");
        }
        discountDto.setDiscountIdOfDiscountModel();
        Discount discount = discountRepository.save(modelMapper.map(discountDto, fcodelol.clone.juno.repository.entity.Discount.class));
        List<DiscountModelDto> discountModelDtoList = discountDto.getDiscountModelDtoList();
        for (DiscountModelDto discountModelDto : discountModelDtoList) {
            discountModelDto.setDiscount(new DiscountByGroupDto(discount.getId()));
            discountModelRepository.save(modelMapper.map(discountModelDto, DiscountModel.class));
        }
        if (discount.getCode() == null) //discount without code
        {
            futureMap.put(discount.getId() * 2, taskScheduler.schedule(runStartEvent(discount.getStartTime(), discount.getId())
                    , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
            futureMap.put(discount.getId() * 2 + 1, taskScheduler.schedule(runFinishEvent(discount.getStartTime(), discount.getId())
                    , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
        }
        logger.info("Add discount success");
        return new Response(200, "Add discount success");
    }

    public Response updateDiscount(DiscountDto discountDto) {
        logger.info("Update discount: " + discountDto);
        discountDto.setDiscountIdOfDiscountModel();
        Discount discount = discountRepository.findOneByIdAndIsDisable(discountDto.getId(), false);
        Timestamp startTime = discount.getStartTime();
        Timestamp finishTime = discount.getEndTime();
        if (discount == null) {
            logger.warn("Discount is not exist");
            throw new CustomException(404, "This discount is not exist");
        }
        discount = modelMapper.map(discountDto, fcodelol.clone.juno.repository.entity.Discount.class);
        discount.setIsDisable(false);
        discount = discountRepository.save(discount);
        if (discount.getCode() == null) //discount without code
        {
            if (discount.getStartTime() != startTime)
                futureMap.put(discount.getId() * 2, taskScheduler.schedule(runStartEvent(discount.getStartTime(), discount.getId())
                        , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
            if (discount.getEndTime() != finishTime)
                futureMap.put(discount.getId(), taskScheduler.schedule(runFinishEvent(discount.getStartTime(), discount.getId())
                        , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
        }
        logger.info("Update discount success");
        return new Response(200, "Update discount success");
    }

    @Transactional
    public Response removeDiscountById(int discountId) {
        logger.info("Remove discount by id" + discountId);
        Discount discount = discountRepository.getById(discountId);
        if (discount.getCode() == null) //discount without code
        {
            futureMap.get(discountId * 2).cancel(false);
            futureMap.get(discountId * 2 + 1).cancel(false);
            futureMap.remove(discountId * 2);
            futureMap.remove(discountId * 2 + 1);
        }
        discount.setIsDisable(true);
        discountRepository.save(discount);
        logger.info("Remove discount success");
        return new Response(200, "Remove discount success");
    }

    @Transactional
    public Response removeDiscountModel(int discountModelId) {
        logger.info("Remove discount model by id" + discountModelId);
        DiscountModel discountModel = discountModelRepository.findOneById(discountModelId);
        if (discountModel == null) return new Response(404, "This discount model is not exists");
        Discount discount = discountModel.getDiscount();
        if (discount.getStartTime().getTime() <= System.currentTimeMillis())
            return new Response(405, "This event started, cannot change");
        discountModelRepository.deleteById(discountModel.getId());
        logger.info("Remove discount-model success");
        return new Response(200, "Remove discount model success");
    }

    @Transactional
    public Runnable runStartEvent(Timestamp startTime, int discountId)  {
        return () -> {
            Discount discount = discountRepository.findOneByIdAndIsDisable(discountId, false);
            if (discount.getStartTime().compareTo(startTime) != 0) return;
            for (DiscountModel discountModel : discount.getDiscountModelList()) {
                Model model = discountModel.getModel();
                if (discountModel.getPrice() != null) {
                    model.setDiscountPrice(discountModel.getPrice());
                } else {
                    if (discount.getPrice() != null)
                        model.setDiscountPrice(discount.getPrice());
                    if (discount.getPercent() != null)
                        model.setDiscountPrice(model.getPrice().multiply(BigDecimal.valueOf((double) discount.getPercent() / 100d)));
                }
                modelRepository.save(model);
                futureMap.get(discountId * 2).cancel(false);
                futureMap.remove(discountId * 2);
            }
        };
    }


    public Runnable runFinishEvent(Timestamp finishTime, int discountId) {
        return () -> {
            Discount discount = discountRepository.findOneByIdAndIsDisable(discountId, false);
            if (discount == null) return;
            if (discount.getEndTime().compareTo(finishTime) != 0) return;
            for (DiscountModel discountModel : discount.getDiscountModelList()) {
                Model model = discountModel.getModel();
                model.setPrice(discount.getPrice());
                modelRepository.save(model);
            }
            futureMap.get(discountId * 2 + 1).cancel(false);
            futureMap.remove(discountId * 2 + 1);
        };
    }

    public Response<List<DiscountByGroupDto>> getAllDiscount() {
        List<DiscountByGroupDto> discountByGroupDtoList = discountRepository.findByIsDisable(false, Sort.by(Sort.Direction.DESC, "startTime"))
                .stream().map(discount -> modelMapper.map(discount, DiscountByGroupDto.class)).collect(Collectors.toList());
        return new Response(200, "Success", discountByGroupDtoList);
    }


    public Response<DiscountResponse> getDiscountById(int id) {
        Discount discount = discountRepository.findOneByIdAndIsDisable(id, false);
        DiscountResponse discountResponse = modelMapper.map(discount, DiscountResponse.class);
        discountResponse.setDiscountModelDtoList(discount.getDiscountModelList().stream()
                .map(discountModel -> modelMapper.map(discountModel, DiscountModelResponse.class)).collect(Collectors.toList()));
        return new Response(200, "Success", discountResponse);
    }

    private String covertTimestampToCronExpression(Timestamp timestamp) {
        LocalDateTime time =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()),
                        TimeZone.getDefault().toZoneId());
        return String.format("%s %s %s %s %s ?", time.getSecond(), time.getMinute(), time.getHour(), time.getDayOfMonth(), convertTimestampMonthToNumber(time.getMonth()));
    }

    private String convertTimestampMonthToNumber(Month month) {
        return month.name().substring(0, 3);
    }

    private boolean validDiscountTime(Timestamp startTime, Timestamp endTime) {
        if (startTime.getTime() < System.currentTimeMillis()) return false;
        if (startTime.getTime() >= endTime.getTime()) return false;
        return true;
    }

}
