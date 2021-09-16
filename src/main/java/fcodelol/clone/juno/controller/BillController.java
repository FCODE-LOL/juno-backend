package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/bill")
public class BillController {
    @Autowired
    BillService billService;

    @PutMapping(value = "/{id}")
    public BillByGroupDto setBillStatus(@PathVariable int id, @RequestBody int status) {
        return billService.setBillStatus(id, status);
    }

    @PutMapping(value = "/delete/{id}")
    public String deleteBill(@PathVariable int id) {
        return billService.deleteBill(id);
    }

    @GetMapping
    public List<BillByGroupDto> getAllBill() {
        return billService.getAllBill();
    }

    @GetMapping(value = "/{id}")
    public BillDto getBillById(@PathVariable int id) {
        return billService.getBillById(id);
    }
}
