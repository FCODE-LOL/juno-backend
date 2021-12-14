package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.*;
import fcodelol.clone.juno.exception.CustomException;
import fcodelol.clone.juno.repository.ModelRepository;
import fcodelol.clone.juno.repository.ProductRepository;
import fcodelol.clone.juno.repository.TypeRepository;
import fcodelol.clone.juno.repository.entity.Product;
import fcodelol.clone.juno.repository.entity.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Transactional
    public Response<ProductDto> addProduct(ProductDto productDto) {
        logger.info("Add product: " + productDto);
        if (productRepository.existsByIdAndIsDisable(productDto.getId(), false))
            return new Response(500, "Product id already exists");
        productDto.setProductIdOfModel();
        Product product = modelMapper.map(productDto, Product.class);
        product.setIsDisable(false);
        logger.info("Add product success:" + productDto);
        return new Response(200, "success", modelMapper.map(productRepository.save(product), ProductDto.class));
    }

    public Response<ProductDto> updateProduct(ProductDto productDto) {
        logger.info("Update product:" + productDto);
        Product product = productRepository.findByIdAndIsDisable(productDto.getId(), false);
        ProductDto productResponse;
        if (product == null) {
            return new Response(404, "product id is not exist");
        }
        product = modelMapper.map(productDto, Product.class);
        product.setIsDisable(false);
        productResponse = modelMapper.map(productRepository.save(product), ProductDto.class);
        logger.info("Update product success:" + productDto);
        return new Response(200, "success", productResponse);

    }


    public Response deleteProduct(String id) {
        logger.info("Delete product:" + id);
        Product product = productRepository.findByIdAndIsDisable(id, false);
        if (product != null) {
            product.setIsDisable(Boolean.TRUE);
            productRepository.save(product);
        } else {
            logger.warn("Delete not exist product");
            throw new CustomException(404, "Product id is not exist");
        }
        return new Response(200, "Delete successful");
    }

    @Transactional
    public Response<List<ProductByGroupDto>> getProductByType(int typeId, Comparator<ProductByGroupDto> comparator) {
        List<ProductByGroupDto> productByGroupDtoList = new ArrayList<>();
        List<Type> types = typeRepository.findByParentId(typeId);
        Type currentType = typeRepository.findOneById(typeId);
        if (currentType.getParentId() != currentType.getId())
            types.add(currentType);
        for (Type type : types) {
            List<Product> productList = type.getProducts();
            productList.removeIf(product -> product.getIsDisable());
            List<ProductByGroupDto> productByGroupDtos =
                    productList.stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
            Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
        }
        Collections.sort(productByGroupDtoList, comparator);
        return new Response(200, "success", productByGroupDtoList);
    }

    @Transactional
    public Response<List<ProductByGroupDto>> getAllProduct() {
        List<ProductByGroupDto> productByGroupDtoList = new LinkedList<>();
        List<ProductByGroupDto> productByGroupDtos =
                productRepository.findByIsDisable(false).stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
        Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
        return new Response(200, "success", productByGroupDtoList);
    }

    public Response<List<ProductByGroupDto>> getProductByName(String name, Sort sort) {
        logger.info("Get product by name:" + name);
        return new Response(200, "success",
                productRepository.findByNameContainingIgnoreCaseAndIsDisable(name, false, sort).stream()
                        .map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList()));
    }

    public Response<ProductDto> getProductById(String id) {
        logger.info("Get product with id: " + id);
        Product product = productRepository.findOneById(id);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        productDto.setModelList(product.getModelList().stream().map(model ->
        {
            model.setProduct(null);
            return modelMapper.map(model, ModelDto.class);
        }).collect(Collectors.toList()));
        logger.info("Get product: " + productDto);
        return new Response(200, "success", productDto);
    }

    public Response<List<TypeDto>> getType() {
        List<TypeDto> typeDtoList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(type -> modelMapper.map(type, TypeDto.class)).collect(Collectors.toList());
        return new Response(200, "Success", typeDtoList);
    }

    @Transactional
    public Response addTypeList(List<TypeDto> addList) {
        logger.info("Start add type list");
        long index = typeRepository.count();
        for (TypeDto addedTypeDto : addList)
            if (!typeRepository.existsByName(addedTypeDto.getName())) {
                Type type = modelMapper.map(addedTypeDto, Type.class);
                index++;
                if (type.getParentId() == 0) {
                    type.setParentId((int) index);
                }
                typeRepository.save(type);
                logger.info(addedTypeDto);
            } else {
                logger.info("Add type list:" + addedTypeDto.getName() + " already exists");
                throw new CustomException(500, addedTypeDto.getName() + " already exists");
            }
        logger.info("Add type success");
        return new Response(200, "Add type success");
    }

    @Transactional
    public Response updateTypeList(List<TypeDto> updateList) {
        logger.info("Start update type list");
        for (TypeDto typeDto : updateList) {
            if (typeRepository.existsByName(typeDto.getName())) {
                logger.info("Update type: " + typeDto.getId() + "is not exist");
                throw new CustomException(404, typeDto.getId() + "is not exist");
            }
            typeRepository.save(modelMapper.map(typeDto, Type.class));
        }
        logger.info("Update type success");
        return new Response(200, "Update type success");
    }

    @Transactional
    public Response deleteType(int typeId) {
        logger.info("Delete type with id: " + typeId);
        if (!typeRepository.existsById(typeId)) {
            logger.info("Delete type is not exist");
            throw new CustomException(404, "Type is not exists");
        }
        List<Type> typeList = typeRepository.findByParentId(typeId);
        for (Type type : typeList) {
            List<Product> productList = type.getProducts();
            if (productList != null) {
                productList.forEach(product -> product.setType(null));
                productRepository.saveAll(productList);
            }
        }
        Collections.reverse(typeList);
        typeList.forEach(type -> typeRepository.deleteById(type.getId()));
        logger.info("Delete type success");
        return new Response(200, "Delete type success");
    }
}