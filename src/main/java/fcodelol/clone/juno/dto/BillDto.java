package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDto extends BillByGroupDto {

    private List<BillModelDto> billModelDtoList;

    @Override
    public String toString() {
        String billConvert = "";
        for(BillModelDto billModelDto : billModelDtoList)
            billConvert += billModelDto;
        return billConvert + super.toString();
    }
}
