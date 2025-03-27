package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public Event findById(Long eventId) {
        log.info("Searching event with id = {}", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %s, not found", eventId)));
    }

    public Set<Event> findAllByIdIn(Set<Long> ids) {
        log.info("Searching events with ids = {}", ids);
        return eventRepository.findAllByIdIn(ids);
    }

    public Event findByIdAndInitiatorId(Long userId, Long eventId) {
        log.info("Searching event with id = {} for user with id = {}", eventId, userId);
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " and event with id = " + eventId
                        + " not found"));
    }
}
