package com.inventory_management.web.service;

import com.inventory_management.web.dto.GroupDto;
import com.inventory_management.web.entity.Group;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();

    Group findByGroupByName(@NotBlank(message = "Tên không được để trống") @Size(max = 100, message = "Tên phải có độ dài tối đa 100 ký tự") String name);

    void save(@Valid GroupDto groups, List<Long> userIds, List<Long> functionIds);

    GroupDto findByID(Long groupID);

    void updateGroup(@Valid GroupDto groupDto);

    void delete(long groupID);

    List<String> getListUserNamesByID(Long groupId);

    List<String> getListFunctionNamesByID(Long groupId);

    void updateStatusGroup(Long groupIds, String status);

    Page<GroupDto> searchGroups(String name, String status, Pageable pageable);
}
