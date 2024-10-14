package com.inventory_management.web.mapper;

import com.inventory_management.web.entity.GroupUser;
import com.inventory_management.web.dto.GroupUserDto;
import org.springframework.stereotype.Component;

@Component
public class GroupUserMapper {

    // Convert GroupUser entity to GroupUserDto
    public GroupUserDto toDto(GroupUser groupUser) {
        if (groupUser == null) {
            return null;
        }

        return GroupUserDto.builder()
                .id(groupUser.getId())
                .groupId(groupUser.getGroupId())
                .userId(groupUser.getUserId())
                .createdOn(groupUser.getCreatedOn())
                .updatedOn(groupUser.getUpdatedOn())
                .build();
    }

    // Convert GroupUserDto to GroupUser entity
    public GroupUser toEntity(GroupUserDto groupUserDto) {
        if (groupUserDto == null) {
            return null;
        }

        GroupUser groupUser = new GroupUser();
        groupUser.setId(groupUserDto.getId());
        groupUser.setGroupId(groupUserDto.getGroupId());
        groupUser.setUserId(groupUserDto.getUserId());
        groupUser.setCreatedOn(groupUserDto.getCreatedOn());
        groupUser.setUpdatedOn(groupUserDto.getUpdatedOn());

        return groupUser;
    }
}
