package com.inventory_management.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "producttype")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String code;

    private int numOfProduct;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(length = 255)
    private String description;

    @PrePersist
    protected void onCreate() {
        date = new Date();
    }

    // Được gọi trước khi cập nhật bản ghi
    @PreUpdate
    protected void onUpdate() {
        date = new Date();
    }
}
