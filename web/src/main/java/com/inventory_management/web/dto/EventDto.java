package com.inventory_management.web.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private  Long Id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name length must be less than 50 characters")
    private String name;

    @NotNull(message = "Start date cannot be null")
    private LocalDateTime dateBegin;

    @NotNull(message = "End date cannot be null")
    @FutureOrPresent(message = "(*) Ngày kết thúc không được sớm hơn ngày bắt đầu")
    private LocalDateTime dateEnd;

    @Min(value = 0, message = "Sale must be at least 0")
    @Max(value = 100, message = "Sale cannot be more than 100")
    private int sale;

    @Min(value = 0, message = "Số lượng cần mua không thể nhỏ hơn 1")
    private int soLuongCanMuaDeNhan;

    @Min(value = 0, message = "Số lượng đã nhận không thể âm")
    private int soLuongNhan;

}
