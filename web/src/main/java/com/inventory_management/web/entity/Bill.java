package com.inventory_management.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "bill")
@Builder
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    @Column(length = 50)
    private String ten_khach_hang;

    @Column(length = 13)
    private String so_khach_hang;

    private int so_luong_mua;
    private int so_luong_nhan;
    private long gia_goc;
    private long gia_sale;
    private boolean mua_tai_cua_hang;
    private long so_tien_nhan_tu_khach;
    private long so_tien_tra_lai;

    @Column(length = 255)
    private String address;

    private long tien_coc;
    private long tien_ship;
    private long tong_tien_nhan_khi_ship;

    @Column(length = 30)
    private String status;

    private Long eventId;

    private Long productId;

    private Long createdByUserId;

    @PrePersist
    protected void onCreate() {
        date = LocalDateTime.now();
    }

}
