package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "`USER`")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String phone;
    @Column
    private String name;
    @Column(name = "date_of_birth")
    private String dateOfBirth;
    @Column(name = "rank_id")
    private String rankId;
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "area_id")
    private String areaId;
    @Column
    private String address;
    @Column(name = "register_timestamp")
    private Timestamp registerTimestamp;
    @Column
    private String token;
    @Column(name = "token_timestamp")
    private Timestamp tokenTimestamp;
    @Column
    private Integer point;
    @Column
    private String role;
    @Column(name = "is_disabled")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisabled;
}
