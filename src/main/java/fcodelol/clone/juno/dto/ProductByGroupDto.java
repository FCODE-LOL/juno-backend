package fcodelol.clone.juno.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductByGroupDto {
    private String id;
    private String name;
    private String linkImages;
    private String colorsId;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Timestamp createdTimestamp;

    public ProductByGroupDto(Object[] property) {
        this.id = (String)property[0];
        this.name = (String)property[1];
        this.linkImages = (String)property[2];
        this.colorsId = (String)property[3];
        this.price = (BigDecimal)property[4];
        this.discountPrice = (BigDecimal)property[5];
        this.createdTimestamp = (Timestamp)property[6];
    }
}
