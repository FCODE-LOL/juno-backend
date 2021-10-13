package fcodelol.clone.juno.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private TypeDto typeDto;
    private BigDecimal price;
    private BigDecimal discountPrice;
    List<ModelDto> modelDtoList;
}
