package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillByGroupDto {
    private int id;
    private String customerName;
    private String phone;
    private int areaId;
    private String address;
    private String paymentMethod;
    private String discountCode;
    private BigDecimal payment;
    private String transportFee;
    private Timestamp receiveTimestamp;
    private int status;
    private String info;

    public BillByGroupDto(int id) {
        this.id = id;
    }
}
