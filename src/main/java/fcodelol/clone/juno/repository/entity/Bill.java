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
@Table(name = "`BILL`")
@Getter
@Setter
@NoArgsConstructor
public class Bill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "`USER_id`")
    private User user;
    @Column(name = "`customer_name`")
    private String customerName;
    @Column
    private String phone;
    @Column(name = "`area_id`")
    private int areaId;
    @Column
    private String address;
    @Column(name = "`payment_method`")
    private String paymentMethod;
    @Column(name = "`discount_code`")
    private String discountCode;
    @Column
    private BigDecimal payment;
    @Column(name = "`transport_fee`")
    private String transportFee;
    @Column(name = "`created_timestamp`", insertable = false)
    private Timestamp createdTimestamp;
    @Column(name = "`receive_timestamp`")
    private Timestamp receiveTimestamp;
    @Column(name = "`update_timestamp`", updatable = false, insertable = false)
    private Timestamp updateTimestamp;
    @Column
    private Integer status;
    @Column
    private String info;
    @Column(name = "is_disable", insertable = false)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bill", cascade = {CascadeType.ALL})
    private List<BillModel> billModelList;

    public Bill(Integer id) {
        this.id = id;
    }

    public void setBillOfBillProductList(){
        billModelList.forEach(billModel -> billModel.setBill(this));
    }
}
