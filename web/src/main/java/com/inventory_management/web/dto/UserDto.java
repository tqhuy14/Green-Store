package com.inventory_management.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "(*) Tên là bắt buộc")
    @Size(min = 2, max = 50, message = "(*) Họ và tên phải có độ dài từ 2 đến 50 ký tự")
    private String name;

    @NotBlank(message = "Tên người dùng là bắt buộc")
    @Size(min = 3, max = 20, message = "(*) Tên người dùng phải có độ dài từ 3 đến 20 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    @Size(min = 6, message = "(*) Mật khẩu phải có ít nhất 6 ký tự")
    private String password;


    @Email(message = "(*) Email phải hợp lệ")
    private String email;


    @Pattern(regexp = "^$|\\d{10}", message = "(*) Số điện thoại phải có đúng 10 chữ số")
    private String phone;

    private String status;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;


    private String role;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status='" + status + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", role=" + role +
                '}';
    }
}
