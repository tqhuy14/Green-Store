package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.FunctionDto;
import com.inventory_management.web.entity.Function;

public class FunctionMapper {

    // Phương thức chuyển đổi từ FunctionEntity sang FunctionDto
    public static FunctionDto toDto(Function function) {
        if (function == null) {
            return null;
        }

        return FunctionDto.builder()
                .id(function.getId())
                .code(function.getCode())
                .name(function.getName())
                .path(function.getPath())
                .createdOn(function.getCreatedOn())  // Nếu cần thiết
                .updatedOn(function.getUpdatedOn())  // Nếu cần thiết
                .build();
    }

    // Phương thức chuyển đổi từ FunctionDto sang FunctionEntity
    public static Function toEntity(FunctionDto functionDto) {
        if (functionDto == null) {
            return null;
        }

        return Function.builder()
                .id(functionDto.getId())
                .code(functionDto.getCode())
                .name(functionDto.getName())
                .path(functionDto.getPath())
                .createdOn(functionDto.getCreatedOn())  // Nếu cần thiết
                .updatedOn(functionDto.getUpdatedOn())  // Nếu cần thiết
                .build();
    }
}
