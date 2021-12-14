package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ModelDto {
    private Integer id;
    private ProductByGroupDto product;
    private String linkImages;
    private String colorId;
    private Integer size;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
}
