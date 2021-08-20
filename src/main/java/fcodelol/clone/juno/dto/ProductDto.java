package fcodelol.clone.juno.dto;


import fcodelol.clone.juno.repository.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String id;
    private String name;
    private String linkImages;
    private String colorsId;
    private String description;
    private String origin;
    private String material;
    private String sizes;
    private int quantity;
    private Type type;
    private BigDecimal price;
}
