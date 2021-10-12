package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.StatusRequest;
import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.controller.response.BillResponseDto;
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
    public BillByGroupDto setBillStatus(@PathVariable int id, @RequestBody StatusRequest statusRequest) {
        return billService.setBillStatus(id, statusRequest);
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
    public BillResponseDto getBillById(@PathVariable int id) {
        return billService.getBillById(id);
    }
}
