package fcodelol.clone.juno.dto;

import fcodelol.clone.juno.dto.ModelExtendDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DiscountModelResponse {
    private Integer id;
    private ModelExtendDto model;
    private BigDecimal price;
}
