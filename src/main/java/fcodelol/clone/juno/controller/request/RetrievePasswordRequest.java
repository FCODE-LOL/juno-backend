package fcodelol.clone.juno.controller.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RetrievePasswordRequest {
    String email;
    String token;
}
