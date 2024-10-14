package com.inventory_management.web.service.Impl;

import com.inventory_management.web.dto.EventDto;
import com.inventory_management.web.entity.Event;
import com.inventory_management.web.mapper.EventMapper;
import com.inventory_management.web.repository.EventRepository;
import com.inventory_management.web.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    EventRepository eventRepository;
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Page<EventDto> findAll(Pageable pageable) {
        Page<Event> eventsPage = eventRepository.findAll(pageable);
        return eventsPage.map(EventMapper::toDTO);
    }

    @Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(EventMapper :: toDTO).collect(Collectors.toList());
    }

    @Override
    public void saveEvent(EventDto eventDto) {
        Event event = EventMapper.toEntity(eventDto);
        eventRepository.save(event);
    }

    @Override
    public void delete(Long eventID) {
        eventRepository.deleteById(eventID);
    }

    @Override
    public EventDto findByName(String name) {
        Event event = eventRepository.findByName(name);
        return EventMapper.toDTO(event);
    }

    @Override
    public void updateEvent(EventDto eventDto) {
        Event event = EventMapper.toEntity(eventDto);
        eventRepository.save(event);
    }

    @Override
    public Page<EventDto> searchEvents(String name, Pageable pageable) {
        Page<Event> eventsPage = eventRepository.searchEvent(name, pageable);
        return eventsPage.map(EventMapper::toDTO);
    }

    @Override
    public Event findByName1(String name) {
        return eventRepository.findByName(name);
    }

    @Override
    public EventDto findByID(Long eventID) {
        return eventRepository.findById(eventID).map(EventMapper::toDTO).orElse(null);
    }

    @Override
    public List<EventDto> findEventCurrent() {
        List<Event> events = eventRepository.findEventCurrent();
        System.out.println(events+"-----------");
        return events.stream().map(EventMapper::toDTO).collect(Collectors.toList());
    }
}
