package fcodelol.clone.juno.service;

import fcodelol.clone.juno.controller.response.Response;
import fcodelol.clone.juno.dto.*;
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
    ModelRepository modelRepository;
    @Autowired
    TypeRepository typeRepository;
    @Autowired
    ModelMapper modelMapper;
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Transactional
    public Response<ProductDto> addProduct(ProductDto productDto) {
        try {
            if (productRepository.existsByIdAndIsDisable(productDto.getId(), false))
                return new Response(500, "Product id already exists");
            productDto.setProductIdOfModel();
            Product product = modelMapper.map(productDto, Product.class);
            product.setIsDisable(false);
            return new Response(200, "success", modelMapper.map(productRepository.save(product), ProductDto.class));
        } catch (Exception e) {
            logger.error("Add product error: " + e.getMessage());
            return new Response(500, "Add product exception");
        }
    }

    public Response<ProductDto> updateProduct(ProductDto productDto) {
        try {
            Product product = productRepository.findByIdAndIsDisable(productDto.getId(), false);
            if (product == null) {
                return new Response(404, "product id is not exist");
            }
            product = modelMapper.map(productDto, Product.class);
            product.setIsDisable(false);
            return new Response(200, "success", modelMapper.map(productRepository.save(product), ProductDto.class));
        } catch (Exception e) {
            logger.error("Update product error: " + e.getMessage());
            return new Response(500, "Update product failed");
        }
    }


    public Response deleteProduct(String id) {
        try {
            Product product = productRepository.findByIdAndIsDisable(id, false);
            if (product != null) {
                product.setIsDisable(Boolean.TRUE);
                productRepository.save(product);
            } else
                return new Response(404, "Product id is not exist");
            return new Response(200, "Delete successful");
        } catch (Exception e) {
            logger.error("Delete error:" + e.getMessage());
            return new Response(500, "Delete failed");
        }
    }

    @Transactional
    public Response<List<ProductByGroupDto>> getProductByType(int typeId, Comparator<ProductByGroupDto> comparator) {
        try {
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
        } catch (Exception e) {
            logger.error("Get product by type: " + e.getMessage());
            return new Response(500, "Get product by type failed");
        }
    }

    @Transactional
    public Response<List<ProductByGroupDto>> getAllProduct() {
        try {
            List<ProductByGroupDto> productByGroupDtoList = new LinkedList<>();
            List<ProductByGroupDto> productByGroupDtos =
                    productRepository.findByIsDisable(false).stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
            Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
            return new Response(200, "success", productByGroupDtoList);
        } catch (Exception e) {
            logger.error("Get product by type: " + e.getMessage());
            return new Response(500, "Get all product failed");
        }
    }

    public Response<List<ProductByGroupDto>> getProductByName(String name, Sort sort) {
        try {
            return new Response(200, "success",
                    productRepository.findByNameContainingIgnoreCaseAndIsDisable(name, false, sort).stream()
                            .map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            logger.error("Get product by name error: " + e.getMessage());
            return new Response(500, "Get product by name failed");
        }
    }

    public Response<ProductDto> getProductById(String id) {
        try {
            Product product = productRepository.findOneById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            productDto.setModelList(product.getModelList().stream().map(model ->
            {
                model.setProduct(null);
                return modelMapper.map(model, ModelDto.class);
            }).collect(Collectors.toList()));
            return new Response(200, "success", productDto);
        } catch (Exception e) {
            logger.error("Get product by id error: " + e.getMessage());
            return new Response(500, "Get product by id error");
        }
    }

    public Response<List<TypeDto>> getType() {
        try {
            List<TypeDto> typeDtoList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(type -> modelMapper.map(type, TypeDto.class)).collect(Collectors.toList());
            return new Response(200, "Success", typeDtoList);
        } catch (Exception e) {
            logger.error("Get type error:" + e.getMessage());
            return new Response(500, "Get type error");
        }
    }

    @Transactional
    public Response addTypeList(List<TypeDto> addList) {
        try {
            long index = typeRepository.count();
            for (TypeDto addedTypeDto : addList)
                if (!typeRepository.existsByName(addedTypeDto.getName())) {
                    Type type = modelMapper.map(addedTypeDto, Type.class);
                    index++;
                    if (type.getParentId() == 0) {
                        type.setParentId((int) index);
                    }
                    typeRepository.save(type);
                } else {
                    return new Response(500, addedTypeDto.getName() + " already exists");
                }
            return new Response(200, "Add type success");
        } catch (Exception e) {
            logger.error("Add type error:" + e.getMessage());
            return new Response(500, "Add type error");
        }
    }

    @Transactional
    public Response updateTypeList(List<TypeDto> updateList) {
        try {
            for (TypeDto typeDto : updateList) {
                if (typeRepository.existsByName(typeDto.getName()))
                    return new Response(404, typeDto.getId() + "is not exist");
                typeRepository.save(modelMapper.map(typeDto, Type.class));
            }
            return new Response(200, "Update type success");
        } catch (Exception e) {
            logger.error("Update type error:" + e.getMessage());
            return new Response(500, "Update type error");
        }
    }

    @Transactional
    public Response deleteType(int typeId) {
        try {
            if (!typeRepository.existsById(typeId))
                return new Response(404, "Type is not exists");
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
            return new Response(200, "Delete type success");
        } catch (Exception e) {
            logger.error("Delete type error");
            return new Response(500, "Delete type error");
        }
    }
}