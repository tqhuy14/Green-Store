package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.GroupDto;
import com.inventory_management.web.entity.Group;

public class GroupMapper {

    // Chuyển đổi từ Group entity sang GroupDto
    public static GroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }

        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .status(group.getStatus())
                .created_on(group.getCreatedOn())
                .updated_on(group.getUpdatedOn())
                .build();
    }

    // Chuyển đổi từ GroupDto sang Group entity
    public static Group toEntity(GroupDto groupDto) {
        if (groupDto == null) {
            return null;
        }

        return Group.builder()
                .id(groupDto.getId())
                .name(groupDto.getName())
                .description(groupDto.getDescription())
                .status(groupDto.getStatus())
                .createdOn(groupDto.getCreated_on())
                .updatedOn(groupDto.getUpdated_on())
                .build();
    }
}
