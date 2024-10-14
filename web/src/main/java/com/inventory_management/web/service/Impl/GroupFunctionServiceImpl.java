package com.inventory_management.web.service.Impl;

import com.inventory_management.web.repository.GroupFunctionRepository;
import com.inventory_management.web.service.GroupFunctionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupFunctionServiceImpl implements GroupFunctionService {

    GroupFunctionRepository groupFunctionRepository;

    public GroupFunctionServiceImpl(GroupFunctionRepository groupFunctionRepository) {
        this.groupFunctionRepository = groupFunctionRepository;
    }

    @Override
    public List<Long> findFunctionsByGroupId(Long groupID) {
        return groupFunctionRepository.findFunctionsIdsByGroupId(groupID);
    }
}
