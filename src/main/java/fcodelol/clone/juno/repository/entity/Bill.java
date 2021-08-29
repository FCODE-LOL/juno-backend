package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "`BILL`")
@Getter
@Setter
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "customer_name")
    private String customerName;
    @Column
    private String phone;
    @Column(name = "area_id")
    private int areaId;
    @Column
    private String address;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "discount_code")
    private String discountCode;
    @Column
    private BigDecimal payment;
    @Column(name = "transport_fee")
    private String transportFee;
    @Column
    private Integer status;
    @Column(name = "update_timestamp")
    private String updateTimestamp;
    @Column(name = "is_disable", insertable = false)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;
    @OneToMany(fetch = FetchType.LAZY)
    private List<BillProduct> billProducts;
    @OneToMany(fetch = FetchType.LAZY)
    private List<UserBill> userBills;
}
