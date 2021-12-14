package fcodelol.clone.juno.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {
    private String email;
    private String password;
}
