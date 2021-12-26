package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.request.UpdateBillStatusRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.controller.response.BillProductResponse;
import fcodelol.clone.juno.controller.response.BillResponse;
import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.BillModelDto;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.repository.*;
import fcodelol.clone.juno.repository.entity.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillModelRepository billModelRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ShoppingService.class);
    private static final int NOT_CONFIRMED_BILL_STATUS = 0;
    private static final int CONFIRMED_BILL_STATUS = 1;
    private static final int TRANSPORT_BILL_STATUS = 2;
    private static final int COMPLETE_BILL_STATUS = 3;
    private static final int CANCEL_BILL_STATUS = 4;

    @Transactional
    public Response addBill(BillDto billDto) {
        logger.info("Add bill:" + billDto);
        billDto.setBillModelDtoList(setBillProductPrice(billDto.getBillModelDtoList(), billDto.getDiscountCode()));
        billDto.setPayment(calculateBillPrice(billDto.getBillModelDtoList()));
        billDto.getBillModelDtoList().forEach(billModelDto -> {
            Model model = modelRepository.findOneById(billModelDto.getModel().getId());
            if (model.getQuantity() < billModelDto.getQuantity()) {
                logger.warn("Add bill: Not enough quantity of " + billModelDto.getModel());
                throw new IllegalArgumentException("Not enough quantity of " + billModelDto.getModel());
            }
            if (billDto.getStatus() == 1) // customer bought products
            {
                model.setQuantity(model.getQuantity() - billModelDto.getQuantity());
                modelRepository.save(model);
            }
        });
        Bill bill = modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class);
        bill.setBillOfBillProductList();
        billRepository.save(bill);
        logger.info("Add bill success:" + billDto);
        return new Response(200, "Add bill success");
    }


    @Transactional
    public Response updateBillInfo(BillDto billDto) {
        logger.info("Update bill:" + billDto);
        Bill bill = billRepository.findOneById(billDto.getId());
        if (bill == null) {
            logger.warn("Update bill: This bill is not exist");
            throw new CustomException(404, "This bill is not exist");
        }
        if (bill.getStatus() != NOT_CONFIRMED_BILL_STATUS) {
            logger.warn("Update bill: This bill had been confirmed, cannot be changed");
            throw new CustomException(404, "This bill had been confirmed, cannot be changed");
        }
        billDto.setBillModelDtoList(setBillProductPrice(billDto.getBillModelDtoList(), billDto.getDiscountCode()));
        billDto.setPayment(calculateBillPrice(billDto.getBillModelDtoList()));
        List<BillModelDto> billModelDtoList = billDto.getBillModelDtoList();
        if (billModelDtoList != null) {
            for (BillModelDto billModelDto : billModelDtoList) {
                Model model = modelRepository.findOneById(billModelDto.getModel().getId());
                if (model.getQuantity() < billModelDto.getQuantity()) {
                    logger.warn("Update bill: Not enough quantity of " + billModelDto.getModel());
                    throw new IllegalArgumentException("Not enough quantity of " + billModelDto.getModel());
                }
                if (bill.getStatus() == CONFIRMED_BILL_STATUS) // customer bought products
                {
                    model.setQuantity(model.getQuantity() - billModelDto.getQuantity());
                    modelRepository.save(model);
                }
                billModelDto.setBill(new BillByGroupDto(bill.getId()));
            }
        }
        // this function only update bill basic info only, cannot change bill status
        billDto.setStatus(bill.getStatus());
        bill = modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class);
        bill.setIsDisable(false);
        billRepository.save(bill);
        logger.info("Update bill success:" + billDto);
        return new Response(200, "Update bill success");

    }

    public List<BillModelDto> setBillProductPrice(List<BillModelDto> billModelDtoList, String discountCode) {
        if (discountCode != null) {
            Discount discount = discountRepository.findOneByCodeAndIsDisable(discountCode, false);
            if (discount == null) {
                logger.warn("Bill: Wrong discount code");
                throw new CustomException(400, "Wrong discount code");
            }
            for (BillModelDto billModelDto : billModelDtoList) {
                BigDecimal modelOfBillPrice = modelRepository.findDiscountPrice(billModelDto.getModel().getId());
                //check discount by percent or price
                if (discount.getPrice() != null)
                    modelOfBillPrice = modelOfBillPrice.min(discount.getPrice());
                else
                    modelOfBillPrice = modelOfBillPrice.min(modelRepository.findPrice(billModelDto.getModel().getId()).multiply(new BigDecimal(discount.getPercent())));
                billModelDto.setPrice(modelOfBillPrice);
            }
        } else {
            for (BillModelDto billModelDto : billModelDtoList) {
                billModelDto.setPrice(modelRepository.findDiscountPrice(billModelDto.getModel().getId()));
            }
        }
        return billModelDtoList;
    }

    public BigDecimal calculateBillPrice(List<BillModelDto> billModelDtoList) {
        BigDecimal price = new BigDecimal(0);
        for (BillModelDto billModelDto : billModelDtoList) {
            price = price.add(billModelDto.getPrice());
        }
        return price;
    }

    @Transactional
    public Response removeBillById(int billId) {
        logger.info("Remove bill by id: " + billId);
        Bill bill = billRepository.getById(billId);
        bill.setIsDisable(true);
        billRepository.save(bill);
        logger.info("Remove bill by id success");
        return new Response(200, "Remove bill success");
    }


    public Response removeBillProduct(int billProductId) {
        logger.info("Remove bill-product by id: " + billProductId);
        BillModel billModel = billModelRepository.findOneById(billProductId);
        if (billModel == null) {
            logger.warn("Remove bill-product: This id is not exist");
            throw new CustomException(404, "This bill product id is not exist");
        }
        Bill bill = billRepository.findOneById(billModel.getBill().getId());
        if (bill.getStatus() > 0) {
            logger.warn("Remove bill-product: This bill had been confirmed, cannot be changed");
            throw new CustomException(405, "This bill had been confirm, can not be changed");
        }
        billModelRepository.deleteById(billModel.getId());
        bill.setPayment(calculateBillPrice(bill.getBillModelList().stream()
                .map(billModelArgs -> modelMapper.map(billModelArgs, BillModelDto.class)).collect(Collectors.toList())));
        billRepository.save(bill);
        logger.info("Remove bill-product by id success");
        return new Response(200, "Remove bill product success");
    }

    @Transactional
    public Response updateBillStatus(UpdateBillStatusRequest updateBillStatusRequest) {
        logger.info("Update bill status: " + updateBillStatusRequest);
        Bill bill = billRepository.findOneById(updateBillStatusRequest.getBillId());
        if (updateBillStatusRequest.getStatus() == NOT_CONFIRMED_BILL_STATUS) // change bill to unconfirmed status
        {
            logger.warn("Update bill status: Cannot change bill to pre-status");
            throw new CustomException(400, "Cannot change bill to pre-status");
        }
        if (updateBillStatusRequest.getStatus() == CANCEL_BILL_STATUS) //cancel
            getProduct(bill);
        if (updateBillStatusRequest.getStatus() == COMPLETE_BILL_STATUS && bill.getStatus() != COMPLETE_BILL_STATUS) // complete shopping
        {
            User user = bill.getUser();
            user.setPoint(user.getPoint() + (Integer) bill.getPayment().intValue());//increase point\
            bill.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
        }
        if (updateBillStatusRequest.getStatus() != COMPLETE_BILL_STATUS && bill.getStatus() == COMPLETE_BILL_STATUS) // complete shopping
        {
            User user = bill.getUser();
            user.setPoint(user.getPoint() - (Integer) bill.getPayment().intValue());//decrease point
            userRepository.save(user);
        }
        bill.setStatus(updateBillStatusRequest.getStatus());
        bill.setInfo(updateBillStatusRequest.getInfo());
        bill.setReceiveTimestamp(updateBillStatusRequest.getReceiveTimestamp());
        logger.info("Update bill status success");
        return new Response(200, "Update bill success");
    }

    @Transactional
    public void getProduct(Bill bill) {
        for (BillModel billModel : bill.getBillModelList()) {
            Model model = billModel.getModel();
            model.setQuantity(billModel.getQuantity() + model.getQuantity());
            modelRepository.save(model);
        }
    }

    public Integer getUserId(int billId) {
        try {
            return billRepository.getUserIdFromBill(billId);
        } catch (Exception e) {
            logger.error("Get user id by bill id:" + e.getMessage());
            return null;
        }
    }

    public Integer getUserIdByBillProductId(int id) {
        try {
            return billRepository.getUserIdFromBill(billModelRepository.findBillId(id));
        } catch (Exception e) {
            logger.error("Get user id by bill id:" + e.getMessage());
            return null;
        }
    }

    public Response<List<BillByGroupDto>> getAllBill() {
        List<BillByGroupDto> billByGroupDtoList = billRepository.findAll(Sort.by(Sort.Direction.DESC, "updateTimestamp"))
                .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
        return new Response(200, "Success", setProductNamesOfBill(billByGroupDtoList));
    }

    public Response<List<BillByGroupDto>> getBillOfUser(int userId) {
        List<BillByGroupDto> billByGroupDtoList = billRepository.findByUser(new User(userId))
                .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
        return new Response(200, "Success", setProductNamesOfBill(billByGroupDtoList));
    }

    public List<BillByGroupDto> setProductNamesOfBill(List<BillByGroupDto> billByGroupDtoList) {
        return billByGroupDtoList.stream().map(billByGroupDto -> {
            billByGroupDto.setProductNamesOfBill(convertListProductsNameToString(billRepository.getProductNamesFromBill(billByGroupDto.getId())));
            return billByGroupDto;
        }).collect(Collectors.toList());
    }

    public String convertListProductsNameToString(List<String> productNameList) {
        String productNames = "";
        for (String productName : productNameList) {
            productNames += '&' + productName;
        }
        return productNames.substring(1);
    }

    public Response<BillResponse> getBillById(int id) {
        logger.info("Get bill with id: " + id);
        Bill bill = billRepository.findOneByIdAndIsDisable(id, false);
        BillResponse billResponse = modelMapper.map(bill, BillResponse.class);
        billResponse.setProductOfBill(bill.getBillModelList().stream()
                .map(billModel -> modelMapper.map(billModel, BillProductResponse.class)).collect(Collectors.toList()));
        logger.info("Get bill success");
        return new Response(200, "Success", billResponse);
    }
}
