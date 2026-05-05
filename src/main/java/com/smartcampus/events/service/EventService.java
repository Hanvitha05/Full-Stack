package com.smartcampus.events.service;

import com.smartcampus.events.entity.Event;
import com.smartcampus.events.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Page<Event> getAllEvents(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return eventRepository.findAll(pageable);
    }
    
    public Page<Event> filterEvents(LocalDateTime date, String department, String eventType, int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return eventRepository.findByFilters(date, department, eventType, pageable);
    }

    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    public Event getEventById(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        Event event = null;
        if (optional.isPresent()) {
            event = optional.get();
        } else {
            throw new RuntimeException(" Event not found for id :: " + id);
        }
        return event;
    }

    public void deleteEventById(long id) {
        this.eventRepository.deleteById(id);
    }
    
    public List<Event> getAllEventsList() {
        return eventRepository.findAll();
    }
}
