package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.GroupFunctionDto;
import com.inventory_management.web.entity.GroupFunction;

public class GroupFunctionMapper {

    // Chuyển từ entity sang DTO
    public static GroupFunctionDto toDto(GroupFunction groupFunction) {
        if (groupFunction == null) {
            return null;
        }

        return GroupFunctionDto.builder()
                .id(groupFunction.getId())
                .groupId(groupFunction.getGroupId())
                .functionId(groupFunction.getFunctionId())
                .createdOn(groupFunction.getCreatedOn())
                .updatedOn(groupFunction.getUpdatedOn())
                .build();
    }

    // Chuyển từ DTO sang entity
    public static GroupFunction toEntity(GroupFunctionDto groupFunctionDto) {
        if (groupFunctionDto == null) {
            return null;
        }

        GroupFunction groupFunction = GroupFunction.builder()
                .id(groupFunctionDto.getId())
                .groupId(groupFunctionDto.getGroupId())
                .functionId(groupFunctionDto.getFunctionId())
                .createdOn(groupFunctionDto.getCreatedOn())
                .updatedOn(groupFunctionDto.getUpdatedOn())
                .build();

        return groupFunction;
    }
}
