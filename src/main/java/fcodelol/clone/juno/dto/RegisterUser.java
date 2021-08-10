package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUser extends PrimaryUser{
    private String token;

    public RegisterUser(String email,String password,String token) {
        super(email,password);
        this.token = token;
    }
}
