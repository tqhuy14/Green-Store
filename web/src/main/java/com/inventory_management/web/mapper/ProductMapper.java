package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.ProductDto;
import com.inventory_management.web.entity.Product;
import com.inventory_management.web.entity.ProductType;

public class ProductMapper {

    // Convert Product entity to ProductDto
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDate(product.getDate());
        dto.setCode(product.getCode());
        dto.setSo_luong_da_nhap(product.getSo_luong_da_nhap());
        dto.setSo_luong_hien_tai(product.getSo_luong_hien_tai());
        dto.setTong_von_san_pham(product.getTong_von_san_pham());
        dto.setDescription(product.getDescription());
        dto.setImagePath(product.getImagePath());
        dto.setProductTypeId(product.getProductTypeId());

        return dto;
    }

    // Convert ProductDto to Product entity
    public static Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDate(dto.getDate());
        product.setCode(dto.getCode());
        product.setSo_luong_da_nhap(dto.getSo_luong_da_nhap());
        product.setSo_luong_hien_tai(dto.getSo_luong_hien_tai());
        product.setTong_von_san_pham(dto.getTong_von_san_pham());
        product.setDescription(dto.getDescription());
        product.setImagePath(dto.getImagePath());
        product.setProductTypeId(dto.getProductTypeId());

        return product;
    }
}
