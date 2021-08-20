package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ProductByGroupDto {
    private String id;
    private String name;
    private String linkImages;
    private String colorsId;
    private BigDecimal price;
    private Timestamp createdTimestamp;
}
