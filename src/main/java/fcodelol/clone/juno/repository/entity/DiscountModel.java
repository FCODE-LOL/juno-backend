package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "DISCOUNT_MODEL")
@Getter
@Setter
@NoArgsConstructor
public class DiscountModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCOUNT_id")
    private Discount discount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_id")
    private Discount model;
    @Column
    private Integer quantity;
    @Column(name = "is_disable", insertable = false)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;

}
