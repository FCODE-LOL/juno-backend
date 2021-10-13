package fcodelol.clone.juno.repository.entity;

import fcodelol.clone.juno.dto.ProductDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "`PRODUCT`")
@Getter
@Setter
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @Column
    @Access(AccessType.PROPERTY)
    private String id;
    @Column
    private String name;
    @Column(name = "link_images")
    private String linkImages;
    @Column
    private String description;
    @Column
    private String origin;
    @Column
    private String material;
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
    private List<Model> modelList;
    public void setProductProperty(ProductDto productDto){
        ModelMapper modelMapper = new ModelMapper();
        name = productDto.getName();
        linkImages = productDto.getLinkImages();
        description = productDto.getDescription();
        origin = productDto.getOrigin();
        material = productDto.getMaterial();
        price = productDto.getPrice();
        discountPrice = productDto.getDiscountPrice();
        type = modelMapper.map(productDto.getTypeDto(),Type.class);
        modelList = productDto.getModelDtoList().stream().map(modelDto -> modelMapper.map(modelDto,Model.class)).collect(Collectors.toList());
    }

}
