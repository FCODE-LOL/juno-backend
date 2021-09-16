package fcodelol.clone.juno.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class UserByGroupDto {
    private int id;
    private String name;

    public UserByGroupDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserByGroupDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
