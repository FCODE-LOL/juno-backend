package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountModelDto {
    private int id;
    private DiscountByGroupDto discount;
    private ModelDto model;
    private int quantity;
    private BigDecimal price;
}
