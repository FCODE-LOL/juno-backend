package fcodelol.clone.juno.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserByGroupExtendDto {
    private int id;
    private String name;
    private int point;
    private Date registerTimestamp;
    private boolean is_disable;

    public UserByGroupExtendDto(int id, String name, int point, Date registerTimestamp, boolean is_disable) {
        this.id = id;
        this.name = name;
        this.point = point;
        this.registerTimestamp = registerTimestamp;
        this.is_disable = is_disable;
    }



    @Override
    public String toString() {
        return "UserByGroupExtendDto{" +
                "point=" + point +
                ", registerTimestamp=" + registerTimestamp +
                ", is_disable=" + is_disable +
                "} " + super.toString();
    }
}
