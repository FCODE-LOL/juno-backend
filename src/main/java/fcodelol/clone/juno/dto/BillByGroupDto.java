package fcodelol.clone.juno.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private UserByGroupDto user;
    private String customerName;
    private String phone;
    private int areaId;
    private String address;
    private Integer paymentMethod;
    private String discountCode;
    private BigDecimal payment;
    private BigDecimal transportFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Ho_Chi_Minh")
    private Timestamp createdTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Ho_Chi_Minh")
    private Timestamp receiveTimestamp;
    private int status;
    private String info;
    // max number names: three
    private String productNamesOfBill;

    public BillByGroupDto(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BillByGroupDto{" +
                "id=" + id +
                ", user=" + user +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", areaId=" + areaId +
                ", address='" + address + '\'' +
                ", paymentMethod=" + paymentMethod +
                ", discountCode='" + discountCode + '\'' +
                ", payment=" + payment +
                ", transportFee=" + transportFee +
                ", receiveTimestamp=" + receiveTimestamp +
                ", status=" + status +
                ", info='" + info + '\'' +
                '}';
    }
}
