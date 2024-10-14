//package com.inventory_management.web.mapper;
//
//import com.inventory_management.web.dto.RoleDto;
//import org.springframework.stereotype.Component;
//
//import java.util.stream.Collectors;
//import java.util.Collections;
//
//@Component
//public class RoleMapper {
//
//    // Convert from Role entity to RoleDto
//    public static RoleDto toDto(Role role) {
//        return RoleDto.builder()
//                .id(role.getId())
//                .name(role.getName())
//                .createdOn(role.getCreatedOn())
//                .updatedOn(role.getUpdatedOn())
//                // Kiểm tra null cho functionalities
//                .functionalities(role.getFunctionalities() != null
//                        ? role.getFunctionalities().stream()
//                        .map(FunctionalityMapper::toDto)
//                        .collect(Collectors.toList())
//                        : Collections.emptyList()) // Trả về danh sách trống nếu null
//                .build();
//    }
//
//    // Convert from RoleDto to Role entity
//    public static Role toEntity(RoleDto roleDto) {
//        return Role.builder()
//                .id(roleDto.getId())
//                .name(roleDto.getName())
//                .createdOn(roleDto.getCreatedOn())
//                .updatedOn(roleDto.getUpdatedOn())
//                // Kiểm tra null cho functionalities
//                .functionalities(roleDto.getFunctionalities() != null
//                        ? roleDto.getFunctionalities().stream()
//                        .map(FunctionalityMapper::toEntity)
//                        .collect(Collectors.toList())
//                        : Collections.emptyList()) // Trả về danh sách trống nếu null
//                .build();
//    }
//}
