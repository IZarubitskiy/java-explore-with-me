package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.model.Event;

import java.util.Set;

public interface EventService {

    Set<Event> findAllByIdIn(Set<Long> events);
}
