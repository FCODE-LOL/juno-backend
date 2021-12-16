package fcodelol.clone.juno.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {
    private String phone;
    private String name;
    private String dateOfBirth;
    private String areaId;
    private String address;
}
