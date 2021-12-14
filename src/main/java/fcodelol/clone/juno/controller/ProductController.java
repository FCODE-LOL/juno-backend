package fcodelol.clone.juno.controller;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.ProductDto;
import fcodelol.clone.juno.dto.TypeDto;
import fcodelol.clone.juno.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public Response<List<ProductByGroupDto>> getAllProduct() throws Exception {
        return productService.getAllProduct();
    }

    @PostMapping
    public Response<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }

    @PutMapping
    public Response<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @PutMapping(value = "/delete/{id}")
    public Response deleteProduct(@PathVariable String id) {
        return productService.deleteProduct(id);
    }

    @GetMapping(value = "/{id}")
    public Response<ProductDto> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping(value = "/type/{id}")
    public Response<List<ProductByGroupDto>> getProductByTypeId(@PathVariable int id) {
        return productService.getProductByType(id, new Comparator<ProductByGroupDto>() {
            @Override
            public int compare(ProductByGroupDto productByGroupDto1, ProductByGroupDto productByGroupDto2) {
                return productByGroupDto1.getCreatedTimestamp().before(productByGroupDto2.getCreatedTimestamp()) ? 1 : -1;
            }
        });
    }

    @GetMapping(value = "/name/{name}")
    public Response<List<ProductByGroupDto>> getProductByName(@PathVariable String name) {
        return productService.getProductByName(name, Sort.by(Sort.Direction.DESC, "createdTimestamp"));
    }

    @GetMapping(value = "/type")
    public Response<List<TypeDto>> getAllType() {
        return productService.getType();
    }

    @PostMapping(value = "/type")
    public Response addTypes(@RequestBody List<TypeDto> types) {
        return productService.addTypeList(types);
    }

    @PutMapping(value = "/type")
    public Response updateTypes(@RequestBody List<TypeDto> types) {
        return productService.updateTypeList(types);
    }

    @DeleteMapping(value = "/type/{id}")
    public Response deleteType(@PathVariable int id) {
        return productService.deleteType(id);

    }
}
