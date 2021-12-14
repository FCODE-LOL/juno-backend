package fcodelol.clone.juno.controller.response;

import fcodelol.clone.juno.controller.response.BillProductResponse;
import fcodelol.clone.juno.dto.BillByGroupDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillResponse extends BillByGroupDto {

    private List<BillProductResponse> productOfBill;
}
