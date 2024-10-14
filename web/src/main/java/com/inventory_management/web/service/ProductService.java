package com.inventory_management.web.service;

import com.inventory_management.web.dto.ProductDto;
import com.inventory_management.web.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    int getTotalQuantityByProductType(Long productType_id);
    boolean existsByTypeId(long typeID);

    List<ProductDto> findAllProductsDto();

    Product findByName(@NotBlank(message = "(*) Tên sản phẩm là bắt buộc") @Size(max = 50, message = "(*) Tên sản phẩm không được vượt quá 50 ký tự") String name);

    void saveProduct(@Valid ProductDto productDto);

    void delete(long productID);

    void updateProduct(@Valid ProductDto productDto);

    ProductDto findById(Long producID);

    Page<ProductDto> searchProducts(String name, Long startPrice, Long endPrice, Pageable pageable, Integer productTypeId);

    Product findByCode(@NotBlank(message = "(*) Mã sản phẩm là bắt buộc") @Size(max = 10, message = "(*) Mã sản phẩm không được vượt quá 10 ký tự") String code);

    void save(ProductDto product);
}
