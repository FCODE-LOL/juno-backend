package fcodelol.clone.juno.controller.response;

import fcodelol.clone.juno.dto.DiscountByGroupDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
public class DiscountResponse extends DiscountByGroupDto {
    List<DiscountModelResponse> discountModelDtoList;
}
