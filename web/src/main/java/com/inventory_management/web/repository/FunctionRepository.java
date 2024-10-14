package com.inventory_management.web.repository;

import com.inventory_management.web.entity.Function;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FunctionRepository extends JpaRepository<Function, Long> {

    @Query("SELECT f.code FROM Function f")
    List<String> findAllFunctionCodes();
}
