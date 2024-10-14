package com.inventory_management.web.service;

import com.inventory_management.web.dto.EventDto;
import com.inventory_management.web.entity.Event;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface EventService {

    Page<EventDto> findAll(Pageable pageable);

    List<EventDto> findAllEvents();

    void saveEvent(@Valid EventDto eventDto);

    void delete(Long eventID);

    EventDto findByName(String name);

    void updateEvent(@Valid EventDto event);

    Page<EventDto> searchEvents(String name, Pageable pageable);

    Event findByName1(@NotBlank(message = "Name cannot be blank") @Size(max = 50, message = "Name length must be less than 50 characters") String name);

    EventDto findByID(Long eventID);

    List<EventDto> findEventCurrent();
}
