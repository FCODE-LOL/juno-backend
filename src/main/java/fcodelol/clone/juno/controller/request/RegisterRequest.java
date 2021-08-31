package fcodelol.clone.juno.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest extends LoginRequest {
    private String token;

    public RegisterRequest(String email, String password, String token) {
        super(email,password);
        this.token = token;
    }
}
