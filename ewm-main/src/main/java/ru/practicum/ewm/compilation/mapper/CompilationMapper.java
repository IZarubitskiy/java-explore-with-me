package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewm.compilation.dto.CompilationCreateRequest;
import ru.practicum.ewm.compilation.dto.CompilationResponse;
import ru.practicum.ewm.compilation.dto.CompilationUpdateRequest;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;

@Mapper
public interface CompilationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "events")
    @Mapping(target = "pinned", defaultValue = "false")
    Compilation createRequestToCompilation(CompilationCreateRequest compilationCreateRequest, Set<Event> events);

    CompilationResponse compilationToResponse(Compilation compilation);

    @Mapping(target = "events", source = "events")
    @Mapping(target = "pinned", defaultValue = "false")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void compilationUpdateRequest(CompilationUpdateRequest updateCompilationRequest, @MappingTarget Compilation compilation, Set<Event> events);
}