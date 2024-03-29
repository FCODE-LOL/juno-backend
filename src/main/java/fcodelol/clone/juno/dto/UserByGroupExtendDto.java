package fcodelol.clone.juno.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserByGroupExtendDto extends UserByGroupDto  {

    private Integer point;
    private String phone;
    private String address;
    private Date registerTimestamp;
    private Boolean isDisable;


    public UserByGroupExtendDto(int id, String name, Integer point, String phone, String address, Date registerTimestamp, boolean isDisable) {
        super.setId(id);
        super.setName(name);
        this.point = point;
        this.phone = phone;
        this.address = address;
        this.registerTimestamp = registerTimestamp;
        this.isDisable = isDisable;
    }

    @Override
    public String toString() {
        return "UserByGroupExtendDto{" +
                "point=" + point +
                ", registerTimestamp=" + registerTimestamp +
                ", is_disable=" + isDisable +
                "} " + super.toString();
    }
}
