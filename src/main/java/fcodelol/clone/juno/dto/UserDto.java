package fcodelol.clone.juno.dto;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.sql.Timestamp;

public class UserDto {
    private int id;
    private String email;
    private String phone;
    private String name;
    private String dateOfBirth;
    private String areaId;
    private String address;
    private Timestamp registerTimestamp;
    private String token;
    private Timestamp tokenTimestamp;
    private int point;
    private String socialMediaId;
    private boolean isAdmin;
    private boolean isDisable;
}
