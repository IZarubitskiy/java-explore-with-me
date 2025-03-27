package ru.practicum.ewm.compilation.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dao.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationCreateRequest;
import ru.practicum.ewm.compilation.dto.CompilationResponse;
import ru.practicum.ewm.compilation.dto.CompilationUpdateRequest;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.service.CompilationService;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;

import java.util.Collection;

import static ru.practicum.ewm.compilation.dao.CompilationRepository.CompilationSpecification.byPinned;


@Slf4j
@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventService eventService;

    @Override
    public CompilationResponse create(CompilationCreateRequest compilationCreateRequest) {
        Compilation compilation = compilationMapper.createRequestToCompilation(
                compilationCreateRequest,
                eventService.findAllByIdIn(compilationCreateRequest.getEvents()));
        CompilationResponse response = compilationMapper.compilationToResponse(compilationRepository.save(compilation));
        log.info("Compilation with id={} was created", response.getId());
        return response;
    }

    @Override
    public CompilationResponse getCompilationById(Long compId) {
        Compilation compilation = getById(compId);
        log.info("Compilation with id={} was found", compilation.getId());
        return compilationMapper.compilationToResponse(compilation);
    }

    @Override
    public Collection<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);

        Specification<Compilation> specification = Specification.where(byPinned(pinned));
        Page<Compilation> page = compilationRepository.findAll(specification, pageable);

        log.info("Get compilations with {from, size, pinned}={},{},{}", from, size, pinned);

        return page.getContent().stream().map(compilationMapper::compilationToResponse).toList();
    }


    @Override
    public void deleteById(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        log.info("Compilation with id={} was deleted", compilationId);
    }

    @Override
    public CompilationResponse update(Long compilationId, CompilationUpdateRequest compilationUpdateRequest) {
        Compilation compilation = getById(compilationId);
        compilationMapper.compilationUpdateRequest(
                compilationUpdateRequest,
                compilation,
                eventService.findAllByIdIn(compilationUpdateRequest.getEvents()));
        CompilationResponse response = compilationMapper.compilationToResponse(
                compilationRepository.save(compilation));
        log.info("Compilation with id={} was updated", response.getId());
        return response;
    }

    public Compilation getById(Long compilationId) {
        log.info("Searching Compilation with id={}", compilationId);
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d not found", compilationId)));
    }

}