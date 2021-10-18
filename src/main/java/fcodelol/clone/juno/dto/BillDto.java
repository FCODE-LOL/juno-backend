package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDto extends BillByGroupDto {

    private List<BillProductDto> billProductDtoList;
    public void setBillIdOfBillProduct(){
        billProductDtoList.forEach(billProductDto -> billProductDto.setBill(new BillByGroupDto(getId())));
    }
}
