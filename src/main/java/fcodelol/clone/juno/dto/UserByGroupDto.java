package fcodelol.clone.juno.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class UserByGroupDto {
    private Integer id;
    private String name;

    public UserByGroupDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserByGroupDto(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserByGroupDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
