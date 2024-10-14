package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.FunctionDto;
import com.inventory_management.web.entity.Function;
import com.inventory_management.web.entity.Group;
import com.inventory_management.web.mapper.FunctionMapper;
import com.inventory_management.web.repository.FunctionRepository;
import com.inventory_management.web.service.FunctionService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunctionServiceImpl implements FunctionService {
    FunctionRepository functionRepository;

    public FunctionServiceImpl(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    @Override
    public List<FunctionDto> findAll() {
        List<Function> functions = functionRepository.findAll();
        functions.sort(Comparator.comparing(Function::getPath));
        return functions.stream().map(FunctionMapper :: toDto).collect(Collectors.toList());
    }
}
