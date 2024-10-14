package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.entity.UserEntity;

public class UserMapper {

    // Chuyển từ UserEntity sang UserDto
    public static UserDto toDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())  // Lưu ý: Không nên trả về mật khẩu thực trong một số trường hợp
                .email(userEntity.getEmail())
                .status(userEntity.getStatus())
                .phone(userEntity.getPhone())
                .role(userEntity.getRole())
                .createdOn(userEntity.getCreatedOn())
                .updatedOn(userEntity.getUpdatedOn())
                .build();
    }

    // Chuyển từ UserDto sang UserEntity
    public static UserEntity toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        return UserEntity.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(userDto.getPassword())  // Lưu ý: Phải mã hóa mật khẩu khi lưu vào cơ sở dữ liệu
                .email(userDto.getEmail())
                .status(userDto.getStatus())
                .phone(userDto.getPhone())
                .role(userDto.getRole())
                .createdOn(userDto.getCreatedOn())
                .updatedOn(userDto.getUpdatedOn())
                .build();
    }
}
