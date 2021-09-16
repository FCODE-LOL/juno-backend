package fcodelol.clone.juno.service;

import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.repository.BillRepository;
import fcodelol.clone.juno.repository.entity.Bill;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BillService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BillRepository billRepository;
    private static final Logger logger = LogManager.getLogger(BillService.class);

    public BillByGroupDto setBillStatus(int id, int status) {
        try {
            Bill bill = billRepository.findByIdAndIsDisable(id, false);
            bill.setStatus(status);
            return modelMapper.map(billRepository.save(bill), BillByGroupDto.class);
        } catch (Exception e) {
            logger.error("Set status bill error: " + e.getMessage());
            return null;
        }
    }

    public String deleteBill(int id) {
        try {
            Bill bill = billRepository.findByIdAndIsDisable(id, false);
            if (bill == null)
                return "Id is not exist";
            bill.setIsDisable(true);
            billRepository.save(bill);
            return "Delete success";
        } catch (Exception e) {
            logger.error("Delete bill:" + e.getMessage());
            return null;
        }
    }

    public List<BillByGroupDto> getAllBill() {
        try {
            List<BillByGroupDto> billByGroupDtoList = billRepository.findAll(Sort.by(Sort.Direction.DESC, "update_timestamp"))
                    .stream().map(bill -> modelMapper.map(bill, BillByGroupDto.class)).collect(Collectors.toList());
            return billByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get all bill:" + e.getMessage());
            return null;
        }
    }

    public BillDto getBillById(int id) {
        try {
            System.out.println(billRepository.findByIdAndIsDisable(id, false));
            Bill bill = billRepository.findByIdAndIsDisable(id, false);
            BillDto billDto = modelMapper.map(bill, BillDto.class);
            billDto.setProductOfBill(bill.getBillProducts().stream()
                    .map(billProduct -> modelMapper.map(billProduct.getProduct(), ProductByGroupDto.class)).collect(Collectors.toList()));
            return billDto;
        } catch (Exception e) {
            logger.error("Get bill by id error: " + e.getMessage());
            return null;
        }
    }


}
