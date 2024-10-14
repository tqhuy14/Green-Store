package com.inventory_management.web.repository;

import com.inventory_management.web.dto.ProductDto;
import com.inventory_management.web.entity.Product;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProductRepository extends JpaRepository<Product, Long> {

    // Query to get the total quantity of products with the same product type
    @Query("SELECT SUM(p.so_luong_hien_tai) FROM product p WHERE p.productTypeId = :productTypeId")
    Integer getTotalQuantityByProductType(@Param("productTypeId") Long productTypeId);

    Product findByName(String name);
    Product findByCode(String code);

    Product findProductById(Long id);

    @Query("SELECT p FROM product p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:productTypeId IS NULL OR p.productTypeId = :productTypeId) " + // Filter by productTypeId
            "AND (:startPrice IS NULL OR p.price >= :startPrice) " + // Filter by start price
            "AND (:endPrice IS NULL OR p.price <= :endPrice)") // Filter by end price
    Page<Product> searchProduct(@Param("name") String name,
                                 @Param("startPrice") Long startPrice,
                                 @Param("endPrice") Long endPrice,
                                 @Param("productTypeId") Integer productTypeId,
                                 Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE product p SET p.so_luong_hien_tai = p.so_luong_hien_tai - :soLuongMua WHERE p.id = :productId")
    void updateSoLuongHienTai(Long productId, @Positive(message = "Số lượng mua phải lớn hơn 0") int soLuongMua);

    @Query("SELECT SUM(p.tong_von_san_pham) FROM product p")
    Long totalPriceProducts();
}
