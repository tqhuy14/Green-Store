package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.mapper.ProductTypeMapper;
import com.inventory_management.web.entity.ProductType;
import com.inventory_management.web.repository.TypeRepository;
import com.inventory_management.web.service.ProductService;
import com.inventory_management.web.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeServiceImpl implements TypeService {

    TypeRepository typeRepository;
    ProductService productService;

    @Autowired
    public TypeServiceImpl(TypeRepository typeRepository, ProductService productService) {
        this.typeRepository = typeRepository;
        this.productService = productService;
    }

    @Override
    public List<ProductType> findAllProductTypes() {
        List<ProductType> productType = typeRepository.findAll();
        productType.sort(Comparator.comparing(ProductType::getId));
        return productType;
    }

    @Override
    public List<ProductTypeDto> findAllProductTypesDto() {
        List<ProductType> productType = typeRepository.findAll();
        productType.sort(Comparator.comparingLong(ProductType::getId));
        return productType.stream().map(ProductTypeMapper :: toDto).collect(Collectors.toList());
    }

    @Override
    public ProductType findByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public ProductType findByCode(String code) {
        return typeRepository.findByCode(code);
    }

    @Override
    public void saveType(ProductTypeDto typeDto) {
        ProductType productType = ProductTypeMapper.toEntity(typeDto);
        typeRepository.save(productType);
    }

    @Override
    public boolean delete(long typeID) {
        if(productService.existsByTypeId(typeID)) {
            return false;
        }
        typeRepository.deleteById(typeID);
        return true;
    }


    @Override
    public ProductTypeDto findById(long typeID) {
        ProductType productType = typeRepository.findById(typeID).get();
        return ProductTypeMapper.toDto(productType);
    }

    @Override
    public void updateRole(ProductTypeDto typeDto) {
        ProductType type = ProductTypeMapper.toEntity(typeDto);
        typeRepository.save(type);
    }

    @Override
    public ProductType findEntityById(long typeID) {
        return typeRepository.findById(typeID).get();
    }

    @Override
    public Page<ProductTypeDto> searchUsers(String name, Pageable pageable) {
        Page<ProductType> typesPage = typeRepository.searchUser(name, pageable);
        return typesPage.map(ProductTypeMapper::toDto);
    }

}
