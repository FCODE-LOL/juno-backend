package fcodelol.clone.juno.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "`USER`")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
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
    @Column(name = "is_admin")
    private Boolean isAdmin;
    @Column(name = "area_id")
    private String areaId;
    @Column
    private String address;
    @Column(name = "register_timestamp", updatable = false, insertable = false)
    private Timestamp registerTimestamp;
    @Column
    private String token;
    @Column(name = "token_timestamp", updatable = false, insertable = false)
    private Timestamp tokenTimestamp;
    @Column
    private Integer point;
    @Column(name = "social_media_id")
    private String socialMediaId;
    @Column(name = "is_disable", insertable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDisable;
    @OneToMany(mappedBy = "user")
    private List<Bill> billList;
    public User(String name, String socialMediaId) {
        this.name = name;
        this.socialMediaId = socialMediaId;
    }

    public User(Integer id, String name, Integer point ,Timestamp registerTimestamp, Boolean isDisable) {
        this.id = id;
        this.name = name;
        this.registerTimestamp = registerTimestamp;
        this.point = point;
        this.isDisable = isDisable;
    }
}
