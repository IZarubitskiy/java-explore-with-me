package ru.practicum.ewm.controller;

import dto.HitCreateRequest;
import dto.ViewStats;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class StatisticController {
    private final StatisticService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    void addHit(@Valid @RequestBody HitCreateRequest hitCreateRequest) {
        log.info("Post request to /hit");
        statisticsService.createHit(hitCreateRequest);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    Collection<ViewStats> getStatistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("Get request to /stats");
        return statisticsService.getStatistics(start, end, uris, unique);
    }
}