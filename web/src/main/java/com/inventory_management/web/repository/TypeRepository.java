package com.inventory_management.web.repository;

import com.inventory_management.web.entity.ProductType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TypeRepository extends JpaRepository<ProductType, Long> {
    ProductType findByName(String name);
    ProductType findByCode(String code);

    @Query("SELECT p FROM producttype p WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<ProductType> searchUser(@Param("name") String name, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE producttype pt SET pt.numOfProduct = (SELECT COUNT(p.id) FROM product p WHERE p.productTypeId = pt.id)")
    void updateNumOfProduct();
}

