package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "`PRODUCT`")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String name;
    @Column(name = "link_images")
    private String linkImages;
    @Column(name = "colors_id")
    private String colorsId;
    @Column(name = "TYPE_id")
    private int typeId;
    @Column(name = "description")
    private String description;
    @Column
    private String origin;
    @Column
    private String material;
    @Column
    private String sizes;
    @Column
    private int quantity;
    @Column
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_id")
    private  Type type;
    @Column(name = "created_timestamp", updatable = false, insertable = false)
    private Timestamp createdTimestamp;
}
