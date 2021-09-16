package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDto extends BillByGroupDto{
    public BillDto(int id, String customerName, String phone, int areaId, String address, String paymentMethod, String discountCode, BigDecimal payment, String transportFee, Timestamp createdTimestamp, Timestamp receiveTimestamp, Timestamp updateTimestamp, int status, String info, List<ProductByGroupDto> productOfBill) {
        super(id, customerName, phone, areaId, address, paymentMethod, discountCode, payment, transportFee, createdTimestamp, receiveTimestamp, updateTimestamp, status, info);
        this.productOfBill = productOfBill;
    }

    private List<ProductByGroupDto> productOfBill;
}
