package com.inventory_management.web.service;

import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    void saveUser (UserDto userDto);
    void updateUser (UserDto user);

    UserEntity findByEmail(@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email);
    UserEntity findByUsername( String username );
    UserEntity findByPhone(String phone);

    Page<UserDto> searchUsers(String name, String status, Pageable pageable);

    void delete(long userID);
    UserDto findByID(long userID);
    Page<UserDto> findAllUsersPage(int page, int size);
}
