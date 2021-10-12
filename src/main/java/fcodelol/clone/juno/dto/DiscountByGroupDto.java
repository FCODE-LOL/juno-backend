package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountByGroupDto {
    private Integer id;
    private String code;
    private String price;
    private String percent;
    private Timestamp startTime;
    private Timestamp endTime;

    public DiscountByGroupDto(Integer id) {
        this.id = id;
    }
}
