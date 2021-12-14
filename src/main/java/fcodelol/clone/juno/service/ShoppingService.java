package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.request.UpdateBillStatusRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.controller.response.BillProductResponse;
import fcodelol.clone.juno.controller.response.BillResponse;
import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.BillProductDto;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.repository.BillModelRepository;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.ModelRepository;
import fcodelol.clone.juno.repository.UserRepository;
import fcodelol.clone.juno.repository.entity.Bill;
import fcodelol.clone.juno.repository.entity.BillModel;
import fcodelol.clone.juno.repository.entity.Model;
import fcodelol.clone.juno.repository.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ShoppingService.class);

    @Transactional
    public Response addBill(BillDto billDto) {
        logger.info("Add bill:" + billDto);
        billDto.setBillProductDtoList(setBillProductPrice(billDto.getBillProductDtoList()));
        billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
        billDto.getBillProductDtoList().forEach(billProductDto -> {
            Model model = modelRepository.findOneById(billProductDto.getModel().getId());
            if (model.getQuantity() < billProductDto.getQuantity()) {
                logger.warn("Add bill: Not enough quantity of " + billProductDto.getModel());
                throw new IllegalArgumentException("Not enough quantity of " + billProductDto.getModel());
            }
            if (billDto.getStatus() == 1) // customer bought products
            {
                model.setQuantity(model.getQuantity() - billProductDto.getQuantity());
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
    public Response updateBill(BillDto billDto) {
        logger.info("Update bill:" + billDto);
        Bill bill = billRepository.findOneById(billDto.getId());
        if (bill == null) {
            logger.warn("Update bill: This bill is not exist");
            throw new CustomException(404, "This bill is not exist");
        }
        if (bill.getStatus() != 0) {
            logger.warn("Update bill: This bill had been confirmed, cannot be changed");
            throw new CustomException(404, "This bill had been confirmed, cannot be changed");
        }
        billDto.setBillProductDtoList(setBillProductPrice(billDto.getBillProductDtoList()));
        billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
        List<BillProductDto> billProductDtoList = billDto.getBillProductDtoList();
        if (billProductDtoList != null) {
            for (BillProductDto billProductDto : billProductDtoList) {
                Model model = modelRepository.findOneById(billProductDto.getModel().getId());
                if (model.getQuantity() < billProductDto.getQuantity()) {
                    logger.warn("Update bill: Not enough quantity of " + billProductDto.getModel());
                    throw new IllegalArgumentException("Not enough quantity of " + billProductDto.getModel());
                }
                if (bill.getStatus() == 1) // customer bought products
                {
                    model.setQuantity(model.getQuantity() - billProductDto.getQuantity());
                    modelRepository.save(model);
                }
                billProductDto.setBill(new BillByGroupDto(bill.getId()));
            }
        }
        bill = modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class);
        bill.setIsDisable(false);
        billRepository.save(bill);
        logger.info("Update bill success:" + billDto);
        return new Response(200, "Update bill success");

    }

    public List<BillProductDto> setBillProductPrice(List<BillProductDto> billProductDtoList) {
        for (BillProductDto billProductDto : billProductDtoList) {
            billProductDto.setPrice(modelRepository.findDiscountPrice(billProductDto.getModel().getId()));
        }
        return billProductDtoList;
    }

    public BigDecimal calculateBillPrice(List<BillProductDto> billProductDtoList) {
        BigDecimal price = new BigDecimal(0);
        for (BillProductDto billProductDto : billProductDtoList) {
            price = price.add(billProductDto.getPrice());
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
                .map(billModelArgs -> modelMapper.map(billModelArgs, BillProductDto.class)).collect(Collectors.toList())));
        billRepository.save(bill);
        logger.info("Remove bill-product by id success");
        return new Response(200, "Remove bill product success");
    }

    @Transactional
    public Response updateBillStatus(UpdateBillStatusRequest updateBillStatusRequest) {
        logger.info("Update bill status: " + updateBillStatusRequest);
        Bill bill = billRepository.findOneById(updateBillStatusRequest.getBillId());
        if (updateBillStatusRequest.getStatus() == 0) // change bill to unconfirmed status
        {
            logger.warn("Update bill status: Cannot change bill to pre-status");
            throw new CustomException(400, "Cannot change bill to pre-status");
        }
        if (updateBillStatusRequest.getStatus() == 6) //cancel
            getProduct(bill);
        if (updateBillStatusRequest.getStatus() == 5 && bill.getStatus() != 5) // complete shopping
        {
            User user = bill.getUser();
            user.setPoint(user.getPoint() + (Integer) bill.getPayment().intValue());//increase point
            userRepository.save(user);
        }
        if (updateBillStatusRequest.getStatus() != 5 && bill.getStatus() == 5) // complete shopping
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
            return new Response(200, "Success", billByGroupDtoList);
    }

    public Response<List<BillByGroupDto>> getBillOfUser(int userId) {
            List<BillByGroupDto> billByGroupDtoList = billRepository.findByUser(new User(userId))
                    .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
            return new Response(200, "Success", billByGroupDtoList);
    }

    public Response<BillResponse> getBillById(int id) {
        try {

            Bill bill = billRepository.findOneByIdAndIsDisable(id, false);
            BillResponse billResponse = modelMapper.map(bill, BillResponse.class);
            billResponse.setProductOfBill(bill.getBillModelList().stream()
                    .map(billModel -> modelMapper.map(billModel, BillProductResponse.class)).collect(Collectors.toList()));
            return new Response(200, "Success", billResponse);
        } catch (Exception e) {
            logger.error("Get bill by id error: " + e.getMessage());
            return new Response(500, "Get bill by id error");
        }
    }

}
