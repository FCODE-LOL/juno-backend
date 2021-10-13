package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.UpdateBillStatusRequest;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.service.AuthorizationService;
import fcodelol.clone.juno.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/buy")
public class ShoppingController {
    @Autowired
    ShoppingService shoppingService;
    @Autowired
    AuthorizationService authorizationService;
    @PostMapping(value = "/add")
    public String addBill(@RequestHeader String token,@RequestBody BillDto billDto) {
        if (authorizationService.getUserIdByToken(token) != billDto.getUser().getId())
            return "This is not your bill";
        return shoppingService.addBill(billDto);
    }
    @PutMapping(value = "/update")
    public String updateBill(@RequestHeader String token,@RequestBody BillDto billDto) {
        Integer userId = shoppingService.getUserId(billDto.getId());
        if (authorizationService.getUserIdByToken(token) != userId)
            return "This is not your bill";
        billDto.setUser(new UserByGroupDto(userId));
        return shoppingService.updateBill(billDto);
    }
    @PutMapping(value = "/delete/bill/{id}")
    public String removeBill(@PathVariable int billId) {
        return shoppingService.removeBillById(billId);
    }
    @DeleteMapping(value = "/delete/product/{id}")
    public String removeBillProduct(@RequestHeader String token,@PathVariable int billProductId){
        if (authorizationService.getUserIdByToken(token) != shoppingService.getUserIdByBillProductId(billProductId))
            return "This is not your bill";
        return shoppingService.removeBillProduct(billProductId);
    }
    @PutMapping(value = "/update/status")
    public String updateStatus(@RequestBody UpdateBillStatusRequest updateBillStatusRequest){
        return shoppingService.updateBillStatus(updateBillStatusRequest);
    }
}
