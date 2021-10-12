package fcodelol.clone.juno.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBillStatusRequest {
    private int billId;
    private Timestamp receiveTimestamp;
    private int status;
    private String info;
}
