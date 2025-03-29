package ru.practicum.ewm.request.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface RequestService {
    Collection<ParticipationRequestDto> getAllUserRequest(@PathVariable Long userId);

    ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId);

    ParticipationRequestDto cancelRequest(@PathVariable Long userId, @RequestParam Long requestId);
}