//package com.inventory_management.web.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import lombok.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Objects;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class RoleDto {
//    private Long id;
//
//    @NotBlank(message = "Role name is required")
//    private String name;
//    private LocalDateTime createdOn;
//    private LocalDateTime updatedOn;
//
//    @NotEmpty(message = "(*)Bạn cần chọn ít nhất 1 chức nằng cho vai trò")
//    private List<Long> functionalitiesIds;
//
//    private List<FunctionalityDto> functionalities;  // Removed @NotEmpty validation
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RoleDto roleDto = (RoleDto) o;
//        return id != null && id.equals(roleDto.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public String toString() {
//        return "RoleDto{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", createdOn=" + createdOn +
//                ", updatedOn=" + updatedOn +
//                ", functionalitiesIds=" + functionalitiesIds +
//                ", functionalities=" + functionalities +
//                '}';
//    }
//}
