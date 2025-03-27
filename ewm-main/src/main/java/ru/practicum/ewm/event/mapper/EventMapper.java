package ru.practicum.ewm.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.Event;

@Mapper
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", expression = "java(ru.practicum.explore_with_me.event.model.enums.EventState.PENDING)")
    @Mapping(target = "participantLimit", source = "participantLimit", defaultValue = "0")
    Event toEvent(EventCreateRequest eventCreateRequest);

    EventFullDto toFullDto(Event event);

    EventShortDto toShortDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category.id", source = "category")
    void updateUserRequest(EventUpdateUserRequest eventUpdateUserRequest, @MappingTarget Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category.id", source = "category")
    void patchUserRequest(EventUpdateAdminRequest eventUpdateAdminRequest, @MappingTarget Event event);
}