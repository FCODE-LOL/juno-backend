package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountByGroupDto {
    private Integer id;
    private String code;
    private BigDecimal price;
    private int percent;
    private Timestamp startTime;
    private Timestamp endTime;

    public DiscountByGroupDto(Integer id) {
        this.id = id;
    }
}
