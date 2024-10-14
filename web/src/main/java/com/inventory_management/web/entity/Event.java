package com.inventory_management.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "event")
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(name = "date_begin")
    private LocalDateTime dateBegin;

    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    private int sale;

    private int so_luong_can_mua_de_nhan;

    private int so_luong_nhan;


}
