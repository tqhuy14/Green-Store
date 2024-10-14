package com.inventory_management.web.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupUserService {

    List<Long> findUserIdsByGroupId(Long groupID);
}
