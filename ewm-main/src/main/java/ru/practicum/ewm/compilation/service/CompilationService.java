package ru.practicum.ewm.compilation.service;


import ru.practicum.ewm.compilation.dto.CompilationCreateRequest;
import ru.practicum.ewm.compilation.dto.CompilationResponse;
import ru.practicum.ewm.compilation.dto.CompilationUpdateRequest;

import java.util.Collection;

public interface CompilationService {
    CompilationResponse create(CompilationCreateRequest createCompilationRequest);

    void deleteById(Long compilationId);

    CompilationResponse getCompilationById(Long compId);

    Collection<CompilationResponse> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationResponse update(Long compilationId, CompilationUpdateRequest updateCompilationRequest);
}