package fcodelol.clone.juno.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BillDto {
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
    private Timestamp createdTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Asia/Ho_Chi_Minh")
    private Timestamp receiveTimestamp;
    private int status;
    private String info;
    private List<BillModelDto> billModelDtoList;

    @Override
    public String toString() {
        String billConvert = "BillDto{" +
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
                ", info='" + info + '\'';

        // add products in bill
        StringBuilder builder = new StringBuilder();
        builder.append(billConvert);
        for (BillModelDto billModelDto : billModelDtoList)
            builder.append(billModelDto);
        return builder.toString();
    }
}
