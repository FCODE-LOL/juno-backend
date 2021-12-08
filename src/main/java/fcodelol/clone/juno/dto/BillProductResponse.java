package fcodelol.clone.juno.dto;


import fcodelol.clone.juno.dto.ModelExtendDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  BillProductResponse {
    private Integer id;
    private ModelExtendDto model;
    private Integer quantity;
    private BigDecimal price;
}
