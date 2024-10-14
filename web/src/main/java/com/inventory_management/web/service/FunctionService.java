package com.inventory_management.web.service;

import com.inventory_management.web.dto.FunctionDto;

import java.util.List;

public interface FunctionService {
    List<FunctionDto> findAll();
}
