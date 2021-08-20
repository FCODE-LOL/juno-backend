package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddedTypeDto {
    private String name;
    private int parentId;

    public AddedTypeDto(String name) {
        this.name = name;
    }
}
