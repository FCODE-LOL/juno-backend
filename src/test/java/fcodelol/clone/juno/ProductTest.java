package fcodelol.clone.juno;

import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.TypeDto;
import fcodelol.clone.juno.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

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
       // Assert.assertEquals("Add product success",productService.addProduct(new ProductDto("SE123","a","s","b","d","d","e","g",12,new TypeDto(1),new BigDecimal(20),new BigDecimal(20))));
    }
    @Test
    public void updateProduct(){
        //Assert.assertEquals("Update product success",productService.updateProduct(new ProductDto("SE124","c","s","b","d","d","e","g",12,new TypeDto(1),new BigDecimal(20),new BigDecimal(20))));
    }
    @Test
    public void deleteProduct(){
        Assert.assertEquals("Delete successful",productService.deleteProduct("SE123"));
    }


    @Test
    public void addType(){
        List<TypeDto> typeDtoList = new LinkedList<>();
        typeDtoList.add(new TypeDto("b",1));
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
