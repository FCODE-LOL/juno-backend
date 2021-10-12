package fcodelol.clone.juno.service;


import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.BillProductDto;
import fcodelol.clone.juno.repository.BillProductRepository;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.ModelRepository;
import fcodelol.clone.juno.repository.entity.Bill;
import fcodelol.clone.juno.repository.entity.BillProduct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ShoppingService {
    @Autowired
    BillRepository billRepository;
    @Autowired
    BillProductRepository billProductRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ShoppingService.class);

    @Transactional
    public String addBill(BillDto billDto) {
        try {
            billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
            int billId = billRepository.save(modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class)).getId();
            List<BillProductDto> billProductDtoList = billDto.getBillProductDtoList();
            billProductDtoList.forEach(billProductDto -> billProductDto.setId(billId));
            addBillProductList(billDto.getBillProductDtoList());
            return "Add bill success";
        } catch (Exception e) {
            return "Add bill exception:" + e.getMessage();
        }
    }

    @Transactional
    public List<BillProductDto> addBillProductList(List<BillProductDto> billProductDtoList) {
        try {
            billProductDtoList.forEach(billProductDto -> billProductRepository.save(modelMapper.map(billProductDto, BillProduct.class)));
            return billProductDtoList;
        } catch (Exception e) {
            logger.error("Add products of bill exception:" + e.getMessage());
            return null;
        }
    }

    @Transactional
    public String updateBill(BillDto billDto) {
        try {
            if (!billRepository.existsById(billDto.getId()))
                return "This bill is not exist";
            billDto.setPayment(calculateBillPrice(billDto.getBillProductDtoList()));
            Bill bill = modelMapper.map(billDto, fcodelol.clone.juno.repository.entity.Bill.class);
            bill.setIsDisable(false);
            int billId = billRepository.save(bill).getId();
            List<BillProductDto> billProductDtoList = billDto.getBillProductDtoList();
            if (billProductDtoList != null) {
                billProductDtoList.forEach(billProductDto -> billProductDto.setId(billId));
                updateBillProductList(billDto.getBillProductDtoList());
            }
            return "Update bill success";
        } catch (Exception e) {
            return "Add bill exception:" + e.getMessage();
        }
    }

    @Transactional
    public List<BillProductDto> updateBillProductList(List<BillProductDto> billProductDtoList) {
        try {

            for (BillProductDto billProductDto : billProductDtoList) {
                if (!billProductRepository.existsById(billProductDto.getId()))
                    billProductRepository.save(modelMapper.map(billProductDto, BillProduct.class));
            }
            ;
            return billProductDtoList;
        } catch (Exception e) {
            logger.error("Add products pf bill exception:" + e.getMessage());
            return null;
        }
    }

    public BigDecimal calculateBillPrice(List<BillProductDto> billProductDtoList) {
        BigDecimal price = new BigDecimal(0);
        for (BillProductDto billProductDto : billProductDtoList) {
            price = price.add(modelRepository.findDiscountPrice(billProductDto.getModel().getId()));
        }
        return price;
    }

    @Transactional
    public String removeBillById(int billId) {
        try {
            Bill bill = billRepository.getById(billId);
            bill.setIsDisable(true);
            List<BillProduct> billProductList = bill.getBillProductList();
            billProductList.forEach(billProduct -> billProduct.setIsDisable(true));
            return "Remove bill success";
        } catch (Exception e) {
            return "Remove bill" + e.getMessage();
        }
    }

    public String removeBillProductById(int id) {
        try {
            billProductRepository.deleteById(id);
            return "Remove bill product success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
