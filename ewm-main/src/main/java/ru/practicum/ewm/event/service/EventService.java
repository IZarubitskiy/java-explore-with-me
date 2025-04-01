package ru.practicum.ewm.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface EventService {
    Collection<EventShortDto> getAllEvents(Long userId, Integer from, Integer size);

    Collection<EventFullDto> getAllEventsAdmin(EventGetRequestAdmin eventGetRequestAdmin);

    Collection<EventShortDto> getAllEventsPublic(EventGetRequestPublic eventGetRequestPublic);

    EventFullDto patchEventById(Long eventId, EventUpdateRequestAdmin eventUpdateRequestAdmin);

    EventFullDto createEvent(Long userId, EventCreateRequest eventCreateRequest);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest httpServletRequest);

    EventFullDto updateEvent(Long userId, Long eventId, EventUpdateRequestUser eventUpdateRequestUser);

    Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}