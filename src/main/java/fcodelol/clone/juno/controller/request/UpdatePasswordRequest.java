package fcodelol.clone.juno.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdatePasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}
