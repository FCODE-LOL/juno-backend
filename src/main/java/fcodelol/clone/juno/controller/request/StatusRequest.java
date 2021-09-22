package fcodelol.clone.juno.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatusRequest {
    private int status;
    private String info;
}
