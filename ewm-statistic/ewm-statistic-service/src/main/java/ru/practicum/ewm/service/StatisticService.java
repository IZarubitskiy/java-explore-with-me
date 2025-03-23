package ru.practicum.ewm.service;

import dto.HitCreateRequest;
import dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticService {

    void createHit(HitCreateRequest hitCreateRequest);

    Collection<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
