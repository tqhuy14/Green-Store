package com.inventory_management.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private long price;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String code;
    private int so_luong_da_nhap;
    private int so_luong_hien_tai;
    private long tong_von_san_pham;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String imagePath;

    private Long productTypeId;

}
