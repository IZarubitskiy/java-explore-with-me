package ru.practicum.ewm.event.utils;

import ru.practicum.ewm.event.model.Event;

import java.util.Set;

public interface EventSearchUtil {
    Event findByIdAndInitiatorId(Long userId, Long eventId);

    Event findById(Long eventId);

    Set<Event> findAllByIdIn(Set<Long> ids);
}