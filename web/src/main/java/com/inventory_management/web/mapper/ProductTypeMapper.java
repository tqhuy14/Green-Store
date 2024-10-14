package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.entity.ProductType;

public class ProductTypeMapper {
    // Chuyển đổi từ ProductType sang ProductTypeDto
    public static ProductTypeDto toDto(ProductType productType) {
        if (productType == null) {
            return null;
        }

        ProductTypeDto dto = new ProductTypeDto();
        dto.setId(productType.getId());
        dto.setName(productType.getName());
        dto.setCode(productType.getCode());
        dto.setNumOfProduct(productType.getNumOfProduct());
        dto.setDate(productType.getDate());
        dto.setDescription(productType.getDescription());

        return dto;
    }

    // Chuyển đổi từ ProductTypeDto sang ProductType
    public static ProductType toEntity(ProductTypeDto productTypeDto) {
        if (productTypeDto == null) {
            return null;
        }

        ProductType productType = new ProductType();
        productType.setId(productTypeDto.getId());
        productType.setName(productTypeDto.getName());
        productType.setCode(productTypeDto.getCode());
        productType.setNumOfProduct(productTypeDto.getNumOfProduct());
        productType.setDate(productTypeDto.getDate());
        productType.setDescription(productTypeDto.getDescription());

        return productType;
    }
}
