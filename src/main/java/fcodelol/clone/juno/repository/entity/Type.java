package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "`TYPE`")
@Getter
@Setter
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type")
    private List<Product> products;
}
