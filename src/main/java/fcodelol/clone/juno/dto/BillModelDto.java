package fcodelol.clone.juno.dto;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BillModelDto {
    private Integer id;
    private BillByGroupDto bill;
    private ModelDto model;
    private Integer quantity;
    private BigDecimal price;
}
