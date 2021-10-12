package fcodelol.clone.juno.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class DiscountDto extends DiscountByGroupDto {

    private List<DiscountModelDto> discountModelDtoList;
}
