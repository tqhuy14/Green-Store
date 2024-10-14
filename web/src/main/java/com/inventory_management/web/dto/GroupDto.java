package com.inventory_management.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDto {

    private Long id;

    @NotBlank(message = "Tên không được để trống") // Tên không được rỗng hoặc chỉ chứa khoảng trắng
    @Size(max = 100, message = "Tên phải có độ dài tối đa 100 ký tự") // Giới hạn độ dài của tên
    private String name;

    @Size(max = 255, message = "Mô tả phải có độ dài tối đa 255 ký tự") // Giới hạn độ dài của mô tả
    private String description;

    private String status;

    List<Long> userIds;
    List<Long> functionIds;

    List<String> userNames;
    List<String> functionNames;

    private LocalDateTime created_on;
    private LocalDateTime updated_on;

    @Override
    public String toString() {
        return "GroupDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", userIds=" + userIds +
                ", functionIds=" + functionIds +
                ", created_on=" + created_on +
                ", updated_on=" + updated_on +
                '}';
    }
}
