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
public class ModelExtendDto {
    private Integer id;
    private String linkImages;
    private String colorId;
    private Integer size;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private ProductDto product;
}
