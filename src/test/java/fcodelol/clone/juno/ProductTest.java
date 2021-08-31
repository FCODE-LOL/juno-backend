package fcodelol.clone.juno;

import fcodelol.clone.juno.dto.AddedTypeDto;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.ProductDto;
import fcodelol.clone.juno.dto.TypeDto;
import fcodelol.clone.juno.repository.entity.Type;
import fcodelol.clone.juno.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {
    @Autowired
    ProductService productService;
    @Test
    public void addProduct(){
        Assert.assertEquals("Add product success",productService.addProduct(new ProductDto("SE123","a","s","b","d","d","e","g",12,new TypeDto(1),new BigDecimal(20),new BigDecimal(20))));
    }
    @Test
    public void updateProduct(){
        Assert.assertEquals("Update product success",productService.updateProduct(new ProductDto("SE124","c","s","b","d","d","e","g",12,new TypeDto(1),new BigDecimal(20),new BigDecimal(20))));
    }
    @Test
    public void deleteProduct(){
        Assert.assertEquals("Delete successful",productService.deleteProduct("SE123"));
    }
    @Test
    public void getProductByType(){
        List<ProductByGroupDto> productByGroupDtos = productService.getProductByType(1, new Comparator<ProductByGroupDto>() {
            @Override
            public int compare(ProductByGroupDto productByGroupDto1, ProductByGroupDto productByGroupDto2) {
                return productByGroupDto1.getCreatedTimestamp().before(productByGroupDto2.getCreatedTimestamp()) ? 1 : -1;
            }});
        Assert.assertEquals(null,productByGroupDtos);
    }
    @Test
    public void getProductBySort(){
        List<ProductByGroupDto> productByGroupDtos = productService.getProductByName("b",Sort.by(Sort.Direction.DESC,"name"));
        if(productByGroupDtos == null)
            System.out.println("null");
        else System.out.println(productByGroupDtos);
        for (ProductByGroupDto product : productByGroupDtos)
            System.out.println(product);
    }
    @Test
    public void addType(){
        List<AddedTypeDto> typeDtoList = new LinkedList<>();
        typeDtoList.add(new AddedTypeDto("a"));
        typeDtoList.add(new AddedTypeDto("b",1));
        System.out.println(productService.addTypeList(typeDtoList));
    }
    @Test
    public void updateType(){
        List<TypeDto> typeDtoList = new LinkedList<>();
        typeDtoList.add(new TypeDto(1,"b",2));
        typeDtoList.add(new TypeDto(2,"a",1));
        System.out.println(productService.updateTypeList(typeDtoList));
    }
    @Test
    public void deleteType(){
        System.out.println(productService.deleteType(1));
    }
}
