package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.controller.response.DiscountResponse;
import fcodelol.clone.juno.dto.DiscountByGroupDto;
import fcodelol.clone.juno.dto.DiscountDto;
import fcodelol.clone.juno.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/discount")
public class DiscountEventController {

    @Autowired
    DiscountService discountService;

    @PostMapping(value = "/add")
    public Response addDiscount(@RequestBody DiscountDto discountDto) {
        return discountService.addDiscountEvent(discountDto);
    }

    @PutMapping(value = "/update")
    public Response updateDiscount(@RequestBody DiscountDto discountDto) {

        return discountService.updateDiscount(discountDto);
    }

    @PutMapping(value = "/delete/{discountId}")
    public Response removeDiscount(@PathVariable int discountId) {
        return discountService.removeDiscountById(discountId);
    }

    @DeleteMapping(value = "/delete/model/{discountModelId}")
    public Response removeDiscountModel(@PathVariable int discountModelId) {
        return discountService.removeDiscountModel(discountModelId);
    }

    @GetMapping
    public Response<List<DiscountByGroupDto>> getAllDiscount() {
        return discountService.getAllDiscount();
    }

    @GetMapping(value = "/{id}")
    public Response<DiscountResponse> getDiscountById(@PathVariable int id) {
        return discountService.getDiscountById(id);
    }


}

