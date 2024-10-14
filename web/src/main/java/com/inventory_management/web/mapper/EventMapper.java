package com.inventory_management.web.mapper;

import com.inventory_management.web.dto.EventDto;
import com.inventory_management.web.entity.Event;

public class EventMapper {

    // Convert Entity to DTO
    public static EventDto toDTO(Event event) {
        return EventDto.builder()
                .Id(event.getId())
                .name(event.getName())
                .dateBegin(event.getDateBegin())
                .dateEnd(event.getDateEnd())
                .sale(event.getSale())
                .soLuongCanMuaDeNhan(event.getSo_luong_can_mua_de_nhan())
                .soLuongNhan(event.getSo_luong_nhan())
                .build();
    }

    // Convert DTO to Entity
    public static Event toEntity(EventDto eventDTO) {
        return Event.builder()
                .id(eventDTO.getId())
                .name(eventDTO.getName())
                .dateBegin(eventDTO.getDateBegin())
                .dateEnd(eventDTO.getDateEnd())
                .sale(eventDTO.getSale())
                .so_luong_can_mua_de_nhan(eventDTO.getSoLuongCanMuaDeNhan())
                .so_luong_nhan(eventDTO.getSoLuongNhan())
                .build();
    }
}
