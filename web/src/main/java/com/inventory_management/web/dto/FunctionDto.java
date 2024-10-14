package com.inventory_management.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FunctionDto {
    private Long id;
    private String code;
    private String name;
    private Long path;
    private LocalDateTime createdOn;  // Nếu cần thiết
    private LocalDateTime updatedOn;  // Nếu cần thiết
}
