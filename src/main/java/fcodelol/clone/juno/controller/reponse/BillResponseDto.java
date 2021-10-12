package fcodelol.clone.juno.controller.reponse;

import fcodelol.clone.juno.dto.BillByGroupDto;
import fcodelol.clone.juno.dto.ModelExtendDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillResponseDto extends BillByGroupDto {
    public BillResponseDto(int id, String customerName, String phone, int areaId, String address, String paymentMethod, String discountCode, BigDecimal payment, String transportFee, Timestamp receiveTimestamp, int status, String info, List<ModelExtendDto> productOfBill) {
        super(id, customerName, phone, areaId, address, paymentMethod, discountCode, payment, transportFee, receiveTimestamp, status, info);
        this.productOfBill = productOfBill;
    }

    private List<ModelExtendDto> productOfBill;
}
