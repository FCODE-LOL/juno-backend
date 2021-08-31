package fcodelol.clone.juno.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
public class BillDto {
    private int id;
    private String customerName;
    private String phone;
    private int areaId;
    private String address;
    private String paymentMethod;
    private String discountCode;
    private BigDecimal payment;
    private String transportFee;
    private Timestamp create_timestamp;
    private Timestamp update_timestamp;
    private int status;
    private String info;
}
