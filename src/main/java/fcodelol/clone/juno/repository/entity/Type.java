package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`TYPE`")
@Getter
@Setter
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String name;
    @Column(name = "parent_id")
    private Integer parentId;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    private List<Product> products;
    public Type(int id) {
        this.id = id;
    }
}
