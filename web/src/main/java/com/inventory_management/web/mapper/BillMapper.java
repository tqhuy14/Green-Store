package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.BillDto;
import com.inventory_management.web.entity.Bill;

public class BillMapper {

    // Convert Entity to DTO
    public static BillDto toDTO(Bill bill) {
        return BillDto.builder()
                .id(bill.getId())
                .date(bill.getDate())
                .tenKhachHang(bill.getTen_khach_hang())
                .soKhachHang(bill.getSo_khach_hang())
                .soLuongMua(bill.getSo_luong_mua())
                .soLuongNhan(bill.getSo_luong_nhan())
                .giaGoc(bill.getGia_goc())
                .giaSale(bill.getGia_sale())
                .muaTaiCuaHang(bill.isMua_tai_cua_hang())
                .soTienNhanTuKhach(bill.getSo_tien_nhan_tu_khach())
                .soTienTraLai(bill.getSo_tien_tra_lai())
                .address(bill.getAddress())
                .tienCoc(bill.getTien_coc())
                .tienShip(bill.getTien_ship())
                .tongTienNhanKhiShip(bill.getTong_tien_nhan_khi_ship())
                .status(bill.getStatus())
                .eventId(bill.getEventId())
                .productId(bill.getProductId())
                .createdByUserId(bill.getCreatedByUserId())
                .build();
    }

    // Convert DTO to Entity
    public static Bill toEntity(BillDto billDTO) {
        return Bill.builder()
                .id(billDTO.getId())
                .date(billDTO.getDate())
                .ten_khach_hang(billDTO.getTenKhachHang())
                .so_khach_hang(billDTO.getSoKhachHang())
                .so_luong_mua(billDTO.getSoLuongMua())
                .so_luong_nhan(billDTO.getSoLuongNhan())
                .gia_goc(billDTO.getGiaGoc())
                .gia_sale(billDTO.getGiaSale())
                .mua_tai_cua_hang(billDTO.isMuaTaiCuaHang())
                .so_tien_nhan_tu_khach(billDTO.getSoTienNhanTuKhach())
                .so_tien_tra_lai(billDTO.getSoTienTraLai())
                .address(billDTO.getAddress())
                .tien_coc(billDTO.getTienCoc())
                .tien_ship(billDTO.getTienShip())
                .tong_tien_nhan_khi_ship(billDTO.getTongTienNhanKhiShip())
                .status(billDTO.getStatus())
                .eventId(billDTO.getEventId())
                .productId(billDTO.getProductId())
                .createdByUserId(billDTO.getCreatedByUserId())
                .build();
    }
}
