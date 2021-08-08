package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiEntity {
    private String name;
    private String pattern;
    private String httpMethod;
    private String role;
}
