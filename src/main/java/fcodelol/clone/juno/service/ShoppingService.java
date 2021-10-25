package fcodelol.clone.juno.service;


import fcodelol.clone.juno.controller.request.UpdateBillStatusRequest;
import fcodelol.clone.juno.controller.response.BillProductResponse;
import fcodelol.clone.juno.controller.response.BillResponse;
import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.BillProductDto;
import fcodelol.clone.juno.repository.BillProductRepository;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.ModelRepository;
import fcodelol.clone.juno.repository.UserRepository;
import fcodelol.clone.juno.repository.entity.Bill;
import fcodelol.clone.juno.repository.entity.BillProduct;
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
    BillProductRepository billProductRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ExecuteDBService executeDBService;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ShoppingService.class);

    @Transactional
    public String addBill(BillDto billDto) {
        try {

            billDto.setBillProductDtoList(setBillProductPrice(billDto.getBillProductDtoList()));
            billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
            billDto.getBillProductDtoList().forEach(billProductDto -> {
                Model model = modelRepository.findOneById(billProductDto.getModel().getId());
                if (model.getQuantity() < billProductDto.getQuantity())
                    throw new IllegalArgumentException("Not enough quantity of " + billProductDto.getModel());
                if (billDto.getStatus() == 1) // customer bought products
                {
                    model.setQuantity(model.getQuantity() - billProductDto.getQuantity());
                    modelRepository.save(model);
                }
            });
            Bill bill = modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class);
            bill.setBillOfBillProductList();
            billRepository.save(bill);
            return "Add bill success";
        } catch (Exception e) {
            logger.error("Add bill exception:" + e.getMessage());
            return "Add bill exception:" + e.getMessage();
        }
    }


    @Transactional
    public String updateBill(BillDto billDto) {
        try {
            Bill bill = billRepository.findOneById(billDto.getId());
            if (bill == null)
                return "This bill is not exist";
            if (bill.getStatus() != 0)
                return "This bill had been confirmed";
            billDto.setBillProductDtoList(setBillProductPrice(billDto.getBillProductDtoList()));
            billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
            List<BillProductDto> billProductDtoList = billDto.getBillProductDtoList();
            if (billProductDtoList != null) {
                for (BillProductDto billProductDto : billProductDtoList) {
                    Model model = modelRepository.findOneById(billProductDto.getModel().getId());
                    if (model.getQuantity() < billProductDto.getQuantity())
                        throw new Exception("Not enough quantity of " + billProductDto.getModel());
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
            return "Update bill success";
        } catch (Exception e) {
            return "Update bill exception:" + e.getMessage();
        }
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
    public String removeBillById(int billId) {
        try {
            Bill bill = billRepository.getById(billId);
            bill.setIsDisable(true);
            billRepository.save(bill);
            return "Remove bill success";
        } catch (Exception e) {
            return "Remove bill" + e.getMessage();
        }
    }


    public String removeBillProduct(int billProductId) {
        try {

            BillProduct billProduct = billProductRepository.findOneById(billProductId);
            if (billProduct == null) return "This bill product is not exists";
            Bill bill = billRepository.findOneById(billProduct.getBill().getId());
            if (bill.getStatus() > 0)
                return "This bill had been confirm, can not change";
            billProductRepository.deleteById(billProduct.getId());
            bill.setPayment(calculateBillPrice(bill.getBillProductList().stream()
                    .map(billProductArgs -> modelMapper.map(billProductArgs, BillProductDto.class)).collect(Collectors.toList())));
            billRepository.save(bill);
            return "Remove bill product success";
        } catch (Exception e) {
            logger.error("Remove bill error:" + e.getMessage());
            return "Remove bill error:" + e.getMessage();
        }
    }

    @Transactional
    public String updateBillStatus(UpdateBillStatusRequest updateBillStatusRequest) {
        try {
            Bill bill = billRepository.findOneById(updateBillStatusRequest.getBillId());
            if (updateBillStatusRequest.getStatus() == 0) // change bill to unconfirmed status
                throw new Exception("Cannot change bill to pre-status");
            if (updateBillStatusRequest.getStatus() == 6) //cancel
                returnProduct(bill);
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
            return "Update bill success";
        } catch (Exception e) {
            logger.error("Change status error: " + e.getMessage());
            return "Change status error: " + e.getMessage();
        }
    }

    @Transactional
    public void returnProduct(Bill bill) throws Exception {
        for (BillProduct billProduct : bill.getBillProductList()) {
            Model model = billProduct.getModel();
            model.setQuantity(billProduct.getQuantity() + model.getQuantity());
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
            return billRepository.getUserIdFromBill(billProductRepository.findBillId(id));
        } catch (Exception e) {
            logger.error("Get user id by bill id:" + e.getMessage());
            return null;
        }
    }

    public List<BillByGroupDto> getAllBill() {
        try {
            List<BillByGroupDto> billByGroupDtoList = billRepository.findAll(Sort.by(Sort.Direction.DESC, "updateTimestamp"))
                    .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
            return billByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get all bill:" + e.getMessage());
            return null;
        }
    }

    public List<BillByGroupDto> getBillOfUser(int userId) {
        try {
            List<BillByGroupDto> billByGroupDtoList = billRepository.findByUser(new User(userId))
                    .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
            return billByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get all bill of user:" + e.getMessage());
            return null;
        }
    }

    public BillResponse getBillById(int id) {
        try {

            Bill bill = billRepository.findOneByIdAndIsDisable(id, false);
            BillResponse billResponse = modelMapper.map(bill, BillResponse.class);
            billResponse.setProductOfBill(bill.getBillProductList().stream()
                    .map(billProduct -> modelMapper.map(billProduct, BillProductResponse.class)).collect(Collectors.toList()));
            return billResponse;
        } catch (Exception e) {
            logger.error("Get bill by id error: " + e.getMessage());
            return null;
        }
    }

}
