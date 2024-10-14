package com.inventory_management.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank(message = "(*) Tên sản phẩm là bắt buộc")
    @Size(max = 50, message = "(*) Tên sản phẩm không được vượt quá 50 ký tự")
    private String name;

    @Min(value = 0, message = "(*) Giá phải lớn hơn hoặc bằng 0")
    private long price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank(message = "(*) Mã sản phẩm là bắt buộc")
    @Size(max = 10, message = "(*) Mã sản phẩm không được vượt quá 10 ký tự")
    private String code;

    @Min(value = 0, message = "(*) Số lượng nhập phải lớn hơn hoặc bằng 0")
    private int numberGet;

    @Min(value = 0, message = "(*) Tổng vốn sản phẩm phải lớn hơn hoặc bằng 0")
    private Long priceGet;

    private int so_luong_da_nhap;


    private int so_luong_hien_tai;

    private MultipartFile imageFile;

    private long tong_von_san_pham;

    @Size(max = 255, message = "(*) Mô tả không được vượt quá 255 ký tự")
    private String description;

    private String imagePath;

    @NotNull(message = "(*) Loại sản phẩm là bắt buộc")
    private Long productTypeId;

    private ProductTypeDto productTypeDto;


    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", so_luong_da_nhap=" + so_luong_da_nhap +
                ", so_luong_hien_tai=" + so_luong_hien_tai +
                ", tong_von_san_pham=" + tong_von_san_pham +
                ", description='" + description + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", productTypeId=" + productTypeId +
                '}';
    }
}
