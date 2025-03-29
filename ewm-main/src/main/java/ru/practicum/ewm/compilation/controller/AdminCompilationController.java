package ru.practicum.ewm.compilation.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationCreateRequest;
import ru.practicum.ewm.compilation.dto.CompilationResponse;
import ru.practicum.ewm.compilation.dto.CompilationUpdateRequest;
import ru.practicum.ewm.compilation.service.CompilationService;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponse createCompilation(@Valid @RequestBody CompilationCreateRequest compilationCreateRequest) {
        return compilationService.create(compilationCreateRequest);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {
        compilationService.deleteById(compId);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationResponse patchCompilation(@PathVariable Long compId,
                                                @Valid @RequestBody CompilationUpdateRequest compilationUpdateRequest) {
        return compilationService.update(compId, compilationUpdateRequest);
    }
}