package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillProductDto {
    private Integer id;
    private BillByGroupDto bill;
    private ModelDto model;
    private Integer color;
    private Integer quantity;
    private BigDecimal price;
    private String note;
}
