package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "`PRODUCT`")
@Getter
@Setter
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @Column
    private String id;
    @Column
    private String name;
    @Column(name = "link_images")
    private String linkImages;
    @Column(name = "colors_id")
    private String colorsId;
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
    @Column(name = "discount_price")
    private BigDecimal discountPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_id")
    private Type type;
    @Column(name = "created_timestamp", updatable = false, insertable = false)
    private Timestamp createdTimestamp;
    @Column(name = "is_disable", insertable = false)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;
    @OneToMany(fetch = FetchType.LAZY)
    private List<BillProduct> billProducts;
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", linkImages='" + linkImages + '\'' +
                ", colorsId='" + colorsId + '\'' +
                ", description='" + description + '\'' +
                ", origin='" + origin + '\'' +
                ", material='" + material + '\'' +
                ", sizes='" + sizes + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", type=" + type +
                ", createdTimestamp=" + createdTimestamp +
                '}';
    }
}
