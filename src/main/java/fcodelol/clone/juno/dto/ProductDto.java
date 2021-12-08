package fcodelol.clone.juno.dto;


import fcodelol.clone.juno.repository.entity.Product;
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
    private String description;
    private String origin;
    private String material;
    private TypeDto type;
    private BigDecimal price;
    private BigDecimal discountPrice;
    List<ModelDto> modelList;

    public void setProductIdOfModel() {
        modelList.forEach(model -> model.setProduct(new ProductByGroupDto(id)));
    }

}
