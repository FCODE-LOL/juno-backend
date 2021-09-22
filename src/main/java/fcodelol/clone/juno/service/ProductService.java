package fcodelol.clone.juno.service;

import fcodelol.clone.juno.dto.AddedTypeDto;
import fcodelol.clone.juno.dto.ProductByGroupDto;
import fcodelol.clone.juno.dto.ProductDto;
import fcodelol.clone.juno.dto.TypeDto;
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

    public ProductDto addProduct(ProductDto productDto) {
        try {
            if (productRepository.existsByIdAndIsDisable(productDto.getId(), false))
                return null;
            Product product = modelMapper.map(productDto, Product.class);
            return modelMapper.map(productRepository.save(product), ProductDto.class);
        } catch (Exception e) {
            logger.error("Add product error: " + e.getMessage());
            return null;
        }
    }

    public ProductDto updateProduct(ProductDto productDto) {
        try {
            Product product = productRepository.findByIdAndIsDisable(productDto.getId(), false);
            if (product == null) {
                return null;
            }
            product.setProductDtoProperty(productDto);
            return modelMapper.map(productRepository.save(product), ProductDto.class);
        } catch (Exception e) {
            logger.error("Update product error: " + e.getMessage());
            return null;
        }
    }

    public String deleteProduct(String id) {
        try {
            if (!productRepository.existsByIdAndIsDisable(id, false))
                return null;
            Product product = productRepository.findOneById(id);
            if (product != null) {
                product.setIsDisable(Boolean.TRUE);
                productRepository.save(product);
            } else
                return "Product id is not exist";
            return "Delete successful";
        } catch (Exception e) {
            logger.error("Delete error:" + e.getMessage());
            return "Delete failed";
        }
    }

    @Transactional
    public List<ProductByGroupDto> getProductByType(int typeId, Comparator<ProductByGroupDto> comparator) {
        try {
            List<ProductByGroupDto> productByGroupDtoList = new ArrayList<>();
            List<Type> types = typeRepository.findByParentId(typeId);
            for (Type type : types) {
                List<ProductByGroupDto> productByGroupDtos =
                        type.getProducts().stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
                Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
            }
            Collections.sort(productByGroupDtoList, comparator);
            return productByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get product by type: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<ProductByGroupDto> getAllProduct() {
        try {
            List<ProductByGroupDto> productByGroupDtoList = new LinkedList<>();
            List<ProductByGroupDto> productByGroupDtos =
                    productRepository.findAll().stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
            Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
            return productByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get product by type: " + e.getMessage());
            return null;
        }
    }

    public List<ProductByGroupDto> getProductByName(String name, Sort sort) {
        try {
            List<ProductByGroupDto> productByGroupDtos =
                    productRepository.findByNameContainingIgnoreCase(name, sort).stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
            return productByGroupDtos;
        } catch (Exception e) {
            logger.error("Get product by name error: " + e.getMessage());
            return null;
        }
    }

    public ProductDto getProductId(String id) {
        try {
            return modelMapper.map(productRepository.findOneById(id), ProductDto.class);
        } catch (Exception e) {
            logger.error("Get product by id error: ");
            return null;
        }
    }

    public List<TypeDto> getType() {
        try {
            List<TypeDto> typeDtoList = typeRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(type -> modelMapper.map(type, TypeDto.class)).collect(Collectors.toList());
            return typeDtoList;
        } catch (Exception e) {
            logger.error("Get type error:" + e.getMessage());
            return null;
        }
    }

    @Transactional
    public String addTypeList(List<AddedTypeDto> addList) {
        try {
            long index = typeRepository.count();
            for (AddedTypeDto addedTypeDto : addList)
                if (!typeRepository.existsByName(addedTypeDto.getName())) {
                    Type type = modelMapper.map(addedTypeDto, Type.class);
                    if (type.getParentId() == 0) {
                        type.setId((int) ++index);
                        type.setParentId((int) index);
                    }
                    typeRepository.save(type);
                } else {
                    return addedTypeDto.getName() + " already exists";
                }
            return "Add type success";
        } catch (Exception e) {
            logger.error("Add type error:" + e.getMessage());
            return "Add type error: " + e.getMessage();
        }
    }

    @Transactional
    public String updateTypeList(List<TypeDto> updateList) {
        try {
            for (TypeDto typeDto : updateList) {
                if (typeRepository.existsByName(typeDto.getName())) return typeDto.getId() + "is not exist";
                typeRepository.save(modelMapper.map(typeDto, Type.class));
            }
            return "Update type success";
        } catch (Exception e) {
            logger.error("Update type error:" + e.getMessage());
            return "Update type error: " + e.getMessage();
        }
    }

    @Transactional
    public String deleteType(int typeId) {
        try {
            if (!typeRepository.existsById(typeId))
                return "Type is not exists";
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
            return "Delete type success";
        } catch (Exception e) {
            logger.error("Delete type error");
            return "Delete type error: " + e.getMessage();
        }
    }
}