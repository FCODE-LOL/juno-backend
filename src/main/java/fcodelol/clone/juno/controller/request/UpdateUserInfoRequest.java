package fcodelol.clone.juno.controller.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserInfoRequest {
    private String phone;
    private String name;
    private String dateOfBirth;
    private Integer areaId;
    private String address;
}
