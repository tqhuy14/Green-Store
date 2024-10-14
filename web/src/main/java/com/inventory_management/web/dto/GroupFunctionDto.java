package com.inventory_management.web.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupFunctionDto {

    private Long id;
    private Long groupId;
    private Long functionId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
