package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.response.DiscountModelResponse;
import fcodelol.clone.juno.controller.response.DiscountResponse;
import fcodelol.clone.juno.dto.*;
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
        restartAllDiscountEvent();
    }

    @Transactional
    public void clearScheduler() {
        try {
            for (Future future : futureMap.values())
                future.cancel(false);
            futureMap.clear();
        } catch (Exception e) {
            logger.error("CLEAR SCHEDULER FAIL: " + e.getMessage());
            return;
        }
    }

    @Transactional
    public void restartAllDiscountEvent() {
        try {
            List<Discount> discountList = discountRepository.findByIsDisable(false);
            if (discountList == null) return;
            for (Discount discount : discountList)
                if (discount.getCode() == null) //discount without code
                {
                    futureMap.put(discount.getId() * 2, taskScheduler.schedule(runStartEvent(discount.getStartTime(), discount.getId())
                            , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
                    futureMap.put(discount.getId() * 2 + 1, taskScheduler.schedule(runFinishEvent(discount.getStartTime(), discount.getId())
                            , new CronTrigger(covertTimestampToCronExpression(discount.getStartTime()))));
                }
        } catch (Exception e) {
            logger.error("RESTART FAIL: " + e.getMessage());
            return;
        }
    }

    @Transactional
    public String addDiscountEvent(DiscountDto discountDto) {
        try {
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
            return "Add discount success";
        } catch (Exception e) {
            logger.error("Add discount exception:" + e.getMessage());
            return "Add discount exception:" + e.getMessage();
        }
    }

    public String updateDiscount(DiscountDto discountDto) {
        try {
            discountDto.setDiscountIdOfDiscountModel();
            Discount discount = discountRepository.findOneByIdAndIsDisable(discountDto.getId(), false);
            Timestamp startTime = discount.getStartTime();
            Timestamp finishTime = discount.getEndTime();
            if (discount == null)
                return "This discount is not exist";
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
            List<DiscountModelDto> discountModelDtoList = discountDto.getDiscountModelDtoList();
            if (discountModelDtoList != null) {
                for (DiscountModelDto discountModelDto : discountModelDtoList) {
                    discountModelDto.setDiscount(new DiscountByGroupDto(discount.getId()));
                    discountModelRepository.save(modelMapper.map(discountModelDto, DiscountModel.class));
                }
            }
            return "Update discount success";
        } catch (Exception e) {
            return "Update discount exception:" + e.getMessage();
        }
    }

    @Transactional
    public String removeDiscountById(int discountId) {
        try {
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
            return "Remove discount success";
        } catch (Exception e) {
            return "Remove discount" + e.getMessage();
        }
    }

    @Transactional
    public String removeDiscountModel(int discountModelId) {
        try {
            DiscountModel discountModel = discountModelRepository.findOneById(discountModelId);
            if (discountModel == null) return "This discount model is not exists";
            Discount discount = discountModel.getDiscount();
            if (discount.getStartTime().getTime() > System.currentTimeMillis())
                return "This event started, cannot chang";
            discountModelRepository.deleteById(discountModel.getId());
            return "Remove discount model success";
        } catch (Exception e) {
            logger.error("Remove discount error:" + e.getMessage());
            return "Remove discount error:" + e.getMessage();
        }
    }

    @Transactional
    public Runnable runStartEvent(Timestamp startTime, int discountId) throws Exception {
        return () -> {
            Discount discount = discountRepository.findOneByIdAndIsDisable(discountId, false);
            if (discount.getIsDisable()) return;
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


    public Runnable runFinishEvent(Timestamp finishTime, int discountId) throws Exception {
        return () -> {
            Discount discount = discountRepository.findOneByIdAndIsDisable(discountId, false);
            if (discount == null) return;
            if (discount.getIsDisable()) return;
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

    public List<DiscountByGroupDto> getAllDiscount() {
        try {
            List<DiscountByGroupDto> discountByGroupDtoList = discountRepository.findByIsDisable(false, Sort.by(Sort.Direction.DESC, "startTime"))
                    .stream().map(discount -> modelMapper.map(discount, DiscountByGroupDto.class)).collect(Collectors.toList());
            return discountByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get all discount:" + e.getMessage());
            return null;
        }
    }


    public DiscountResponse getDiscountById(int id) {
        try {

            Discount discount = discountRepository.findOneByIdAndIsDisable(id, false);
            DiscountResponse discountResponse = modelMapper.map(discount, DiscountResponse.class);
            discountResponse.setDiscountModelDtoList(discount.getDiscountModelList().stream()
                    .map(discountModel -> modelMapper.map(discountModel, DiscountModelResponse.class)).collect(Collectors.toList()));
            return discountResponse;
        } catch (Exception e) {
            logger.error("Get discount by id error: " + e.getMessage());
            return null;
        }
    }

    private String covertTimestampToCronExpression(Timestamp timestamp) {
        LocalDateTime time =
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()),
                        TimeZone.getDefault().toZoneId());
        logger.error(String.format("%s %s %s %s %s ?", time.getSecond(), time.getMinute(),time.getHour(), time.getDayOfMonth(), convertTimestampMonthToNumber(time.getMonth())));
        return String.format("%s %s %s %s %s ?", time.getSecond(), time.getMinute(),time.getHour(), time.getDayOfMonth(), convertTimestampMonthToNumber(time.getMonth()));
    }

    public String convertTimestampMonthToNumber(Month month){
        return month.name().substring(0,3);
    }

}
