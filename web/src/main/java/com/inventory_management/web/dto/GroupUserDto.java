package com.inventory_management.web.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupUserDto {

    private Long id;
    private Long groupId;
    private Long userId;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
}
