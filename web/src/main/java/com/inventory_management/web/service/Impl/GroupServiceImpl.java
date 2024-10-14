package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.GroupDto;
import com.inventory_management.web.entity.*;
import com.inventory_management.web.mapper.GroupMapper;
import com.inventory_management.web.repository.*;
import com.inventory_management.web.service.GroupService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    GroupRepository groupRepository;
    GroupUserRepository groupUserRepository;
    GroupFunctionRepository groupFunctionRepository;
    UserRepository userRepository;
    FunctionRepository functionRepository;

    public GroupServiceImpl(GroupRepository groupRepository,
                            GroupUserRepository groupUserRepository,
                            GroupFunctionRepository groupFunctionRepository,
                            UserRepository userRepository,
                            FunctionRepository functionRepository) {
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
        this.groupFunctionRepository = groupFunctionRepository;
        this.userRepository = userRepository;
        this.functionRepository = functionRepository;
    }

    @Override
    public List<GroupDto> findAll() {
        List<Group> groups = groupRepository.findAll();
        groups.sort(Comparator.comparing(Group::getId));
        return groups.stream().map(GroupMapper :: toDto).collect(Collectors.toList());
    }

    @Override
    public Group findByGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    @Override
    public void save(GroupDto groupDto, List<Long> userIds, List<Long> functionIds) {
        Group group = GroupMapper.toEntity(groupDto);
        groupRepository.save(group);
        Long groupId = groupRepository.findFirstByOrderByCreatedOnDesc().getId();
        if(userIds != null && functionIds != null && !userIds.isEmpty() && !functionIds.isEmpty()) {
            for (Long userId : userIds) {
                groupUserRepository.save(GroupUser.builder().groupId(groupId).userId(userId).build());
            }

            for (Long functionId : functionIds) {
                groupFunctionRepository.save(GroupFunction.builder().groupId(groupId).functionId(functionId).build());
            }
        }
    }

    @Override
    public GroupDto findByID(Long groupID) {
        Group group = groupRepository.findById(groupID).get();
        return GroupMapper.toDto(group);
    }

    @Override
    @Transactional
    public void updateGroup(GroupDto groupDto) {
        Group group = GroupMapper.toEntity(groupDto);
        groupRepository.save(group);

        // Xóa tất cả các user và function liên quan đến group hiện tại
        try {
            groupUserRepository.deleteByGroupId(groupDto.getId());
            groupFunctionRepository.deleteByGroupId(groupDto.getId());
        }catch (Exception e) {
            System.out.println("0---"+ e.getMessage());
        }

        // Thêm lại các user mới nếu có
        if (groupDto.getUserIds() != null && !groupDto.getUserIds().isEmpty()) {
            for (Long userId : groupDto.getUserIds()) {
                groupUserRepository.save(GroupUser.builder().groupId(groupDto.getId()).userId(userId).build());
            }
        }

        // Thêm lại các function mới nếu có
        if (groupDto.getFunctionIds() != null && !groupDto.getFunctionIds().isEmpty()) {
            for (Long functionId : groupDto.getFunctionIds()) {
                groupFunctionRepository.save(GroupFunction.builder().groupId(groupDto.getId()).functionId(functionId).build());
            }
        }
    }

    @Override
    @Transactional
    public void delete(long groupID) {
        try {
            groupFunctionRepository.deleteByGroupId(groupID);
            groupUserRepository.deleteByGroupId(groupID);
            groupRepository.deleteById(groupID);
        }catch (Exception e) {
            System.out.println("1---"+ e.getMessage());
        }
    }

    @Override
    public List<String> getListUserNamesByID(Long groupId) {
        List<Long> userIds = groupUserRepository.findUserIdsByGroupId(groupId);
        // Tìm danh sách tên người dùng dựa trên userId từ UserEntity
        System.out.println(userIds);
        return userRepository.findAllById(userIds).stream()
                .map(UserEntity::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getListFunctionNamesByID(Long groupId) {
        List<Long> functionIds = groupFunctionRepository.findFunctionsIdsByGroupId(groupId);
        // Tìm danh sách tên người dùng dựa trên userId từ UserEntity
        return functionRepository.findAllById(functionIds).stream()
                .map(Function::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatusGroup(Long groupIds, String status) {
        groupRepository.updateGroupStatus(status, groupIds);
    }

    @Override
    public Page<GroupDto> searchGroups(String name, String status, Pageable pageable) {
        Page<Group> groupsPage = groupRepository.searchGroup(name, status, pageable);
        return groupsPage.map(GroupMapper::toDto);
    }


}
