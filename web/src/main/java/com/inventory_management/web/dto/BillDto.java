package com.inventory_management.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDto {
    private Long id;

    private LocalDateTime date;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 50, message = "Tên khách hàng không được quá 50 ký tự")
    private String tenKhachHang;

    @NotBlank(message = "Số khách hàng không được để trống")
    @Size(max = 13, message = "Số khách hàng không được quá 13 ký tự")
    private String soKhachHang;

    @Positive(message = "Số lượng mua phải lớn hơn 0")
    private int soLuongMua;

    private int soLuongNhan;

    private Long giaGoc;

    private Long giaSale;

    private boolean muaTaiCuaHang;

    private long soTienNhanTuKhach;

    private long soTienTraLai;

    private String address;

    private Long tienCoc;

    private Long tienShip;

    private Long tongTienNhanKhiShip;

    private String status;

    private Long eventId;

    private Long productId;

    private ProductDto product;

    private List<EventDto> events;

    private EventDto event;

    private Long createdByUserId;

    private String paymentMethod;

    private UserDto user;

    @Override
    public String toString() {
        return "BillDto{" +
                "id=" + id +
                ", date=" + date +
                ", tenKhachHang='" + tenKhachHang + '\'' +
                ", soKhachHang='" + soKhachHang + '\'' +
                ", soLuongMua=" + soLuongMua +
                ", soLuongNhan=" + soLuongNhan +
                ", giaGoc=" + giaGoc +
                ", giaSale=" + giaSale +
                ", muaTaiCuaHang=" + muaTaiCuaHang +
                ", soTienNhanTuKhach=" + soTienNhanTuKhach +
                ", soTienTraLai=" + soTienTraLai +
                ", address='" + address + '\'' +
                ", tienCoc=" + tienCoc +
                ", tienShip=" + tienShip +
                ", tongTienNhanKhiShip=" + tongTienNhanKhiShip +
                ", status='" + status + '\'' +
                ", eventId=" + eventId +
                ", productId=" + productId +
                ", product=" + product +
                ", events=" + events +
                ", event=" + event +
                ", createdByUserId=" + createdByUserId +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
