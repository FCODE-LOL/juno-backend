package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.request.UpdateBillStatusRequest;
import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.controller.response.BillResponse;
import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.BillDto;
import fcodelol.clone.juno.dto.UserByGroupDto;
import fcodelol.clone.juno.service.AuthorizationService;
import fcodelol.clone.juno.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/bill")
public class ShoppingController {
    @Autowired
    ShoppingService shoppingService;
    @Autowired
    AuthorizationService authorizationService;

    @PostMapping(value = "/add")
    public Response addBill(@RequestHeader("Authorization") String token, @RequestBody BillDto billDto) {
        billDto.setUser(new UserByGroupDto(authorizationService.getUserIdByToken(token)));
        if (billDto.getUser().getId() == null)
            billDto.setUser(null);
        return shoppingService.addBill(billDto);
    }

    @PutMapping(value = "/update")
    public Response updateBill(@RequestHeader("Authorization") String token, @RequestBody BillDto billDto) {
        Integer userId = shoppingService.getUserId(billDto.getId());
        if (authorizationService.getUserIdByToken(token) != userId)
            return new Response(403, "This is not your bill");
        billDto.setUser(new UserByGroupDto(userId));
        return shoppingService.updateBillInfo(billDto);
    }


//    @PutMapping(value = "/delete/{id}")
//    public Response removeBill(@PathVariable int billId) {
//        return shoppingService.removeBillById(billId);
//    }

    @DeleteMapping(value = "/delete/product/{billProductId}")
    public Response removeBillProduct(@RequestHeader("Authorization") String token, @PathVariable int billProductId) {
        if (authorizationService.getUserIdByToken(token) != shoppingService.getUserIdByBillProductId(billProductId))
            return new Response(403, "This is not your bill");
        return shoppingService.removeBillProduct(billProductId);
    }

    @PutMapping(value = "/update/status")
    public Response updateStatus(@RequestBody UpdateBillStatusRequest updateBillStatusRequest) {
        return shoppingService.updateBillStatus(updateBillStatusRequest);
    }

    @GetMapping
    public Response<List<BillByGroupDto>> getAllBill() {
        return shoppingService.getAllBill();
    }

    @GetMapping(value = "/{id}")
    public Response<BillResponse> getBillById(@RequestHeader("Authorization") String token, @PathVariable int id) {
        if (authorizationService.getRoleByToken(token).equals("ADMIN"))
            return shoppingService.getBillById(id);
        else return shoppingService.getBillById(authorizationService.getUserIdByToken(token));
    }

    @GetMapping(value = "/user/{id}")
    public Response<List<BillByGroupDto>> getAllBillOfUser(@RequestHeader("Authorization") String token, @PathVariable int id) {
        if (authorizationService.getRoleByToken(token).equals("ADMIN"))
            return shoppingService.getBillOfUser(id);
        else return shoppingService.getBillOfUser(authorizationService.getUserIdByToken(token));
    }
}
