package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "`BILL_PRODUCT`")
@Getter
@Setter
@NoArgsConstructor
public class BillProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_id")
    private Product Product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BILL_id")
    private Bill bill;
    @Column
    private Integer quantity;
    @Column
    private Integer color;
    @Column
    private Integer size;
    @Column(name = "is_disable", insertable = false)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;
}
