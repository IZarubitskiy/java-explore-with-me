package ru.practicum.ewm.event.utils.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.utils.EventSearchUtil;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSearchUtilImpl implements EventSearchUtil {
    private final EventRepository eventRepository;

    @Override
    public Event findById(Long eventId) {
        log.info("Searching event with id = {}", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id = %s, not found", eventId)));
    }

    @Override
    public Set<Event> findAllByIdIn(Set<Long> ids) {
        log.info("Searching events with ids = {}", ids);
        return eventRepository.findAllByIdIn(ids);
    }

    @Override
    public Event findByIdAndInitiatorId(Long userId, Long eventId) {
        log.info("Searching event with id = {} for user with id = {}", eventId, userId);
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("User with id = " + userId + " and event with id = " + eventId
                        + " not found"));
    }
}