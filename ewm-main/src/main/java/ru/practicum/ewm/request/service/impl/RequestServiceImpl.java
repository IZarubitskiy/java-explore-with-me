package ru.practicum.ewm.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.utils.EventSearchUtil;
import ru.practicum.ewm.exeption.exemptions.*;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.enums.RequestStatus;
import ru.practicum.ewm.request.service.RequestService;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.utils.UserSearchUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserSearchUtil userSearchUtil;
    private final EventSearchUtil eventSearchUtil;
    private final RequestMapper requestMapper;

    @Override
    public Collection<ParticipationRequestDto> getAllUserRequest(Long userId) {
        userSearchUtil.findUserById(userId);
        Set<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("GET requests by userId = {}", userId);
        return requests.stream().map(requestMapper::toRequestDto).toList();
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        Optional<Request> requestOptional = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requestOptional.isPresent()) {
            throw new DuplicateRequestException("Request can be only one");
        }
        Event event = eventSearchUtil.findById(eventId);
        User user = userSearchUtil.findUserById(userId);
        RequestStatus status = RequestStatus.PENDING;

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotPublishedEventRequestException("Event must be published");
        }

        int requestsSize = requestRepository.findAllByEventId(eventId).size();

        if (event.getParticipantLimit() != 0 && requestsSize >= event.getParticipantLimit()) {
            throw new RequestLimitException("No more seats for the event");
        }

        if (event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        }

        if (event.getInitiator().getId().equals(user.getId())) {
            throw new InitiatorRequestException("Initiator can't submit a request for event");
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status(status)
                .build();
        log.info("POST request body = {}", request);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        userSearchUtil.findUserById(userId);
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request not found"));
        request.setStatus(RequestStatus.CANCELED);
        log.info("Cancel request by requestId = {} and userId = {}", requestId, userId);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }
}
