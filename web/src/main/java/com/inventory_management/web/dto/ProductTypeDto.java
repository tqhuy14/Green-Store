package com.inventory_management.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeDto {

    private Long id; // ID của loại sản phẩm

    @NotBlank(message = "Tên loại sản phẩm không được để trống.")
    @Size(max = 50, message = "Tên loại sản phẩm không được vượt quá 50 ký tự.")
    private String name; // Tên loại sản phẩm

    @NotBlank(message = "Mã loại sản phẩm không được để trống.")
    @Size(max = 10, message = "Mã loại sản phẩm không được vượt quá 10 ký tự.")
    private String code; // Mã loại sản phẩm

    private Integer numOfProduct; // Số lượng sản phẩm

    private Date date; // Ngày tạo

    @Size(max = 255, message = "Mô tả loại sản phẩm không được vượt quá 255 ký tự.")
    private String description; // Mô tả loại sản phẩm

    @Override
    public String toString() {
        return "ProductTypeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", numOfProduct=" + numOfProduct +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
