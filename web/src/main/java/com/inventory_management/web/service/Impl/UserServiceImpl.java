package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.UserDto;
import com.inventory_management.web.mapper.UserMapper;
import com.inventory_management.web.entity.UserEntity;
import com.inventory_management.web.repository.GroupUserRepository;
import com.inventory_management.web.repository.UserRepository;
import com.inventory_management.web.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.inventory_management.web.mapper.UserMapper.toDto;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private GroupUserRepository groupUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, GroupUserRepository groupUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        // Lọc ra những người dùng không phải là admin
        List<UserEntity> nonAdminUsers = users.stream()
                .filter(user -> !user.getRole().equals("admin")) // Kiểm tra vai trò không phải là admin
                .collect(Collectors.toList());

        // Sắp xếp nonAdminUsers theo trường id
        nonAdminUsers.sort(Comparator.comparing(UserEntity::getId));

        return nonAdminUsers.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void saveUser(UserDto userDto) {
        UserEntity user = UserMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }


    @Override
    public void updateUser(UserDto user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        UserEntity origin = userRepository.findById(userEntity.getId()).get();

        if(!userEntity.getPassword().equals(origin.getPassword())) {
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userEntity);
    }


    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity findByPhone(String phone) {
        UserEntity user = userRepository.findByPhone(phone);
        if (user == null) {
            return null;  // Trả về null nếu không tìm thấy
        }
        return user;
    }

    @Override
    public Page<UserDto> searchUsers(String name, String status, Pageable pageable) {
        // Lấy danh sách người dùng dựa trên tên và trạng thái từ repository
        Page<UserEntity> usersPage = userRepository.searchUser(name, status, pageable);

        // Lọc ra những người dùng không phải là admin
        List<UserEntity> nonAdminUsers = usersPage.getContent().stream()
                .filter(user -> !user.getRole().equals("admin")) // Kiểm tra vai trò không phải là admin
                .collect(Collectors.toList());

        // Tạo Page mới với danh sách người dùng đã lọc
        return new PageImpl<>(
                nonAdminUsers.stream().map(UserMapper::toDto).collect(Collectors.toList()),
                pageable,
                usersPage.getTotalElements() // Số lượng tổng vẫn giữ nguyên
        );
    }


    @Override
    public void delete(long userID) {
        groupUserRepository.deleteByUserId(userID);
        userRepository.deleteById(userID);
    }

    @Override
    public UserDto findByID(long userID) {
        UserEntity user = userRepository.findById(userID).get();
        return toDto(user);
    }

    @Override
    public Page<UserDto> findAllUsersPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return userRepository.findAll(pageable).map(user -> toDto(user));
    }


}
