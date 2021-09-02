package fcodelol.clone.juno.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@NoArgsConstructor
public class UserByGroupExtendDto extends UserByGroupDto{
    private int point;
    private Timestamp registerTimestamp;
    private boolean is_disable;
}
