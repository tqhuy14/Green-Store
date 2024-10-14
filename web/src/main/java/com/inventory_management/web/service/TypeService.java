package com.inventory_management.web.service;

import com.inventory_management.web.dto.ProductTypeDto;
import com.inventory_management.web.entity.ProductType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {

    List<ProductType> findAllProductTypes();

    List<ProductTypeDto> findAllProductTypesDto();

    ProductType findByName(@NotBlank(message = "Tên loại sản phẩm không được để trống.") @Size(max = 50, message = "Tên loại sản phẩm không được vượt quá 50 ký tự.") String name);

    ProductType findByCode(@NotBlank(message = "Mã loại sản phẩm không được để trống.") @Size(max = 10, message = "Mã loại sản phẩm không được vượt quá 10 ký tự.") String code);

    void saveType(@Valid ProductTypeDto typeDto);

    boolean delete(long typeID);

    ProductTypeDto findById(long typeID);

    void updateRole(@Valid ProductTypeDto typeDto);

    ProductType findEntityById(long typeID);

    Page<ProductTypeDto> searchUsers(String name, Pageable pageable);
}
