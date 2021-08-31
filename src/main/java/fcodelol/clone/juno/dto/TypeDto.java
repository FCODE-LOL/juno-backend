package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypeDto {
    private int id;
    private String name;
    private int parentId;

    public TypeDto(String name, int parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public TypeDto(int id) {
        this.id = id;
    }
}
