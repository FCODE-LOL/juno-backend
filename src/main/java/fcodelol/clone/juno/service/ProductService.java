package fcodelol.clone.juno.service;

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
    public ProductDto addProduct(ProductDto productDto) {
        try {
            if (productRepository.existsByIdAndIsDisable(productDto.getId(), false))
                return null;
            removeModelOfProductIfExist(productDto.getId());
            Product product = modelMapper.map(productDto, Product.class);
            product.setIsDisable(false);
            return modelMapper.map(productRepository.save(product), ProductDto.class);
        } catch (Exception e) {
            logger.error("Add product error: " + e.getMessage());
            return null;
        }
    }

    private void removeModelOfProductIfExist(String productId) {
        try {
            if (productRepository.existsById(productId)) {
                modelRepository.deleteByProduct(new Product(productId));
            }
        } catch (Exception e) {
            logger.error("Remove model of product:" + e.getMessage());
            return;
        }
    }

    public ProductDto updateProduct(ProductDto productDto) {
        try {
            Product product = productRepository.findByIdAndIsDisable(productDto.getId(), false);
            if (product == null) {
                return null;
            }
            product.setProductProperty(productDto);
            product.setProductOfModel();
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
                    productRepository.findByIsDisable(false).stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
            Optional.ofNullable(productByGroupDtos).ifPresent(productByGroupDtoList::addAll);
            return productByGroupDtoList;
        } catch (Exception e) {
            logger.error("Get product by type: " + e.getMessage());
            return null;
        }
    }

    public List<ProductByGroupDto> getProductByName(String name, Sort sort) {
        try {
            return productRepository.findByNameContainingIgnoreCaseAndIsDisable(name,false, sort).stream().map(product -> modelMapper.map(product, ProductByGroupDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Get product by name error: " + e.getMessage());
            return null;
        }
    }

    public ProductDto getProductById(String id) {
        try {
            Product product = productRepository.findOneById(id);
            ProductDto productDto = modelMapper.map(product, ProductDto.class);
            productDto.setModelList(product.getModelList().stream().map(model ->
            {
                model.setProduct(null);
                return modelMapper.map(model, ModelDto.class);
            }).collect(Collectors.toList()));
            return productDto;
        } catch (Exception e) {
            logger.error("Get product by id error: " + e.getMessage());
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
    public String addTypeList(List<TypeDto> addList) {
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