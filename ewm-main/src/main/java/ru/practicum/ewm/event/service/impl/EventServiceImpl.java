package ru.practicum.ewm.event.service.impl;

import dto.HitCreateRequest;
import dto.ViewStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.client.StatisticClient;
import ru.practicum.ewm.event.dao.EventRepository;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.model.enums.EventStateAction;
import ru.practicum.ewm.event.model.enums.SortType;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.event.utils.EventSearchCriteria;
import ru.practicum.ewm.event.utils.EventSearchUtil;
import ru.practicum.ewm.event.utils.LocationSearchUtil;
import ru.practicum.ewm.exeption.exemptions.*;
import ru.practicum.ewm.request.dao.RequestRepository;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.enums.RequestStatus;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventServiceImpl implements EventService {
    final EventRepository eventRepository;
    final RequestRepository requestRepository;
    final EventMapper eventMapper;
    final RequestMapper requestMapper;
    final EventSearchUtil eventSearchUtil;
    final LocationSearchUtil locationSearchUtil;
    final UserService userService;
    final EntityManager entityManager;
    final StatisticClient statisticClient;

    @Override
    public Collection<EventShortDto> getAllEvents(Long userId, Integer from, Integer size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        Page<Event> page = eventRepository.findAllByInitiatorId(userId, pageable);

        log.info("Get events with {userId, from, size} = ({}, {}, {})", userId, from, size);
        return page.getContent().stream().map(eventMapper::toShortDto).toList();
    }

    @Override
    public Collection<EventFullDto> getAllEventsAdmin(EventGetRequestAdmin eventGetRequestAdmin) {
        Set<Long> users = eventGetRequestAdmin.getUsers();
        Set<String> states = eventGetRequestAdmin.getStates();
        Set<Long> categories = eventGetRequestAdmin.getCategories();
        LocalDateTime rangeStart = eventGetRequestAdmin.getRangeStart();
        LocalDateTime rangeEnd = eventGetRequestAdmin.getRangeEnd();
        Integer from = eventGetRequestAdmin.getFrom();
        Integer size = eventGetRequestAdmin.getSize();

        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);

        Specification<Event> specification = Specification
                .where(EventSearchCriteria.userIn(users))
                .and(EventSearchCriteria.stateIn(states))
                .and(EventSearchCriteria.categoryIn(categories))
                .and(EventSearchCriteria.eventDateAfter(rangeStart))
                .and(EventSearchCriteria.eventDateBefore(rangeEnd));
        Page<Event> page = eventRepository.findAll(specification, pageable);

        log.info("Get events with {users, states, categories, rangeStart, rangeEnd, from, size} = ({},{},{},{},{},{},{})",
                users, size, categories, rangeStart, rangeEnd, from, size);

        List<Request> confirmedRequestsByEventId = requestRepository.findAllByEventIdInAndStatus(
                page.stream().map(Event::getId).toList(), RequestStatus.CONFIRMED);

        Map<Long, List<Request>> eventIdToConfirmedRequests = confirmedRequestsByEventId.stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId()));

        return page.stream()
                .map(event -> {
                    event.setConfirmedRequests(eventIdToConfirmedRequests.getOrDefault(
                            event.getId(),
                            Collections.emptyList()).size());
                    return eventMapper.toFullDto(event);
                })
                .toList();
    }

    @Override
    public Collection<EventShortDto> getAllEventsPublic(EventGetRequestPublic eventGetRequestPublic) {
        String text = eventGetRequestPublic.getText();
        Set<Long> categories = eventGetRequestPublic.getCategories();
        Boolean paid = eventGetRequestPublic.getPaid();
        LocalDateTime rangeStart = eventGetRequestPublic.getRangeStart();
        LocalDateTime rangeEnd = eventGetRequestPublic.getRangeEnd();
        Boolean onlyAvailable = eventGetRequestPublic.getOnlyAvailable();
        SortType sort = eventGetRequestPublic.getSort();
        Integer from = eventGetRequestPublic.getFrom();
        Integer size = eventGetRequestPublic.getSize();
        HttpServletRequest httpServletRequest = eventGetRequestPublic.getHttpServletRequest();

        Pageable pageable = PageRequest.of(from / size, size);

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ValidationException("Time period incorrect");
        }

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);

        Specification<Event> specification = Specification
                .where(EventSearchCriteria.textInAnnotationOrDescription(text))
                .and(EventSearchCriteria.categoryIn(categories))
                .and(EventSearchCriteria.eventDateAfter(rangeStart))
                .and(EventSearchCriteria.eventDateBefore(rangeEnd))
                .and(EventSearchCriteria.isAvailable(onlyAvailable))
                .and(EventSearchCriteria.sortBySortType(sort))
                .and(EventSearchCriteria.onlyPublished());
        Page<Event> page = eventRepository.findAll(specification, pageable);

        saveViewInStatistic("/events", httpServletRequest.getRemoteAddr());

        log.info("Get events with {text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size} = ({},{},{},{},{},{},{},{},{})",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return page.stream()
                .map(eventMapper::toShortDto)
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto patchEventById(Long eventId, EventUpdateRequestAdmin eventUpdateRequestAdmin) {
        Event event = eventSearchUtil.findById(eventId);

        LocalDateTime updateStartDate = eventUpdateRequestAdmin.getEventDate();

        if (event.getState().equals(EventState.PUBLISHED) && LocalDateTime.now().isAfter(event.getPublishedOn().plusHours(1))) {
            throw new PublicationException("Change event no later than one hour before the start");
        }

        if (updateStartDate != null && updateStartDate.isBefore(LocalDateTime.now())) {
            throw new UpdateStartDateException("Date and time has already arrived");
        }

        EventStateAction updateStateAction = getUpdateStateAction(eventUpdateRequestAdmin, event);

        stateChanger(event, updateStateAction);

        if (eventUpdateRequestAdmin.getLocation() != null) {
            Location location = locationSearchUtil.findById(eventUpdateRequestAdmin.getLocation().getLat(),
                    eventUpdateRequestAdmin.getLocation().getLon());
            event.setLocation(location);
        }

        eventMapper.patchUserRequest(eventUpdateRequestAdmin, event);
        if (event.getState().equals(EventState.PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }

        log.info("Patch event with eventId = {}", eventId);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, EventCreateRequest eventCreateRequest) {
        Event event = eventMapper.toEvent(eventCreateRequest);
        Location location = locationSearchUtil.findById(eventCreateRequest.getLocation().getLat(),
                eventCreateRequest.getLocation().getLon());

        event.setInitiator(userService.getUserById(userId));
        event.setLocation(location);

        log.info("Create new event with userId = {}", userId);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        return eventMapper.toFullDto(eventSearchUtil.findByIdAndInitiatorId(userId, eventId));
    }

    @Override
    public EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest httpServletRequest) {
        Event event = eventSearchUtil.findById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new GetPublicEventException("Event must be published");
        }

        saveViewInStatistic("/events/" + eventId, httpServletRequest.getRemoteAddr());

        List<ViewStats> getResponses = loadViewFromStatistic(
                event.getPublishedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId),
                true);
        if (!getResponses.isEmpty()) {
            ViewStats viewStats = getResponses.getFirst();
            event.setViews(viewStats.getHits());
        }

        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, Long eventId, EventUpdateRequestUser eventUpdateRequestUser) {
        Event event = eventSearchUtil.findByIdAndInitiatorId(userId, eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new AlreadyPublishedException("Event with eventId = " + eventId + "has already been published");
        }

        stateChanger(event, eventUpdateRequestUser.getStateAction());
        if (eventUpdateRequestUser.getLocation() != null) {
            Location location = locationSearchUtil.findById(eventUpdateRequestUser.getLocation().getLat(),
                    eventUpdateRequestUser.getLocation().getLon());
            event.setLocation(location);
        }

        eventMapper.updateUserRequest(eventUpdateRequestUser, event);
        log.info("Update event with eventId = {}", eventId);
        return eventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        userService.getUserById(userId);
        Event event = eventSearchUtil.findById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("The event initiator does not match the user id");
        }

        Set<Request> requests = requestRepository.findAllByEventId(eventId);

        return requests.stream().map(requestMapper::toRequestDto).toList();
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequest(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest updateRequest) {
        userService.getUserById(userId);
        Event event = eventSearchUtil.findById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("The event initiator does not match the user id");
        }

        List<Request> requests = requestRepository.findAllByIdIn(updateRequest.getRequestIds());

        for (Request request : requests) {
            if (!request.getEvent().getId().equals(eventId)) {
                throw new NotFoundException("Request with requestId = " + request.getId() + "does not match eventId = " + eventId);
            }
        }

        int confirmedCount = requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();
        int size = updateRequest.getRequestIds().size();
        int confirmedSize = updateRequest.getStatus().equals(RequestStatus.CONFIRMED) ? size : 0;

        if (event.getParticipantLimit() != 0 && confirmedCount + confirmedSize > event.getParticipantLimit()) {
            throw new TooManyRequestsException("Event limit exceed");
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Request request : requests) {
            if (updateRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(requestMapper.toRequestDto(request));
            } else if (updateRequest.getStatus().equals(RequestStatus.REJECTED)) {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new AlreadyConfirmedException("The request cannot be rejected if it is confirmed");
                }
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(requestMapper.toRequestDto(request));
            }
        }

        requestRepository.saveAll(requests);

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private void stateChanger(Event event, EventStateAction stateAction) {
        if (stateAction != null) {
            Map<EventStateAction, EventState> state = Map.of(
                    EventStateAction.SEND_TO_REVIEW, EventState.PENDING,
                    EventStateAction.CANCEL_REVIEW, EventState.CANCELED,
                    EventStateAction.PUBLISH_EVENT, EventState.PUBLISHED,
                    EventStateAction.REJECT_EVENT, EventState.CANCELED);
            event.setState(state.get(stateAction));
        }
    }

    private void saveViewInStatistic(String uri, String ip) {
        HitCreateRequest hitRequest = HitCreateRequest.builder()
                .app("ewm-main-service")
                .uri(uri)
                .ip(ip)
                .build();
        statisticClient.hit(hitRequest);
    }

    private List<ViewStats> loadViewFromStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return statisticClient.getStats(start, end, uris, unique);
    }

    private EventStateAction getUpdateStateAction(EventUpdateRequestAdmin eventUpdateRequestAdmin, Event event) {
        EventStateAction updateStateAction = eventUpdateRequestAdmin.getStateAction();

        if (updateStateAction != null && !event.getState().equals(EventState.PENDING) && updateStateAction.equals(EventStateAction.PUBLISH_EVENT)) {
            throw new PublicationException("The event can only be published during the pending stage");
        }

        if (updateStateAction != null && updateStateAction.equals(EventStateAction.REJECT_EVENT)
                && event.getState().equals(EventState.PUBLISHED)) {
            throw new PublicationException("Cannot reject a published event");
        }
        return updateStateAction;
    }
}