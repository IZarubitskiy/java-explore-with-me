package ru.practicum.ewm.compilation.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationResponse;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.Collection;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponse getCompilationById(@PathVariable(name = "compId") Long compId) {
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationResponse> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }
}