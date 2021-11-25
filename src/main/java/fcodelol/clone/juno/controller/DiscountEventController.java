package fcodelol.clone.juno.controller;

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
    public String addDiscount( @RequestBody DiscountDto discountDto) {
        return discountService.addDiscountEvent(discountDto);
    }

    @PutMapping(value = "/update")
    public String updateDiscount( @RequestBody DiscountDto discountDto) {

        return discountService.updateDiscount(discountDto);
    }

    @PutMapping(value = "/delete/{id}")
    public String removeDiscount(@PathVariable int discountId) {
        return discountService.removeDiscountById(discountId);
    }

    @DeleteMapping(value = "/delete/model/{id}")
    public String removeDiscountModel(@PathVariable int discountModelId) {
        return discountService.removeDiscountModel(discountModelId);
    }

    @GetMapping
    public List<DiscountByGroupDto> getAllDiscount() {
        return discountService.getAllDiscount();
    }

    @GetMapping(value = "/{id}")
    public DiscountResponse getDiscountById(@RequestHeader("Authorization")  String token, @PathVariable int id) {
        return discountService.getDiscountById(id);
    }

    
}

