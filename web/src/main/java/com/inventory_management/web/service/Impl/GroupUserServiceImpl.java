package com.inventory_management.web.service.Impl;

import com.inventory_management.web.repository.GroupUserRepository;
import com.inventory_management.web.service.GroupUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupUserServiceImpl implements GroupUserService {

    GroupUserRepository groupUserRepository;

    public GroupUserServiceImpl(GroupUserRepository groupUserRepository) {
        this.groupUserRepository = groupUserRepository;
    }


    @Override
    public List<Long> findUserIdsByGroupId(Long groupID) {
        return groupUserRepository.findUserIdsByGroupId(groupID);
    }
}
