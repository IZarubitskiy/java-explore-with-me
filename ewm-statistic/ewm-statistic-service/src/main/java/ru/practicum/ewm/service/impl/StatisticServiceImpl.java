package ru.practicum.ewm.service.impl;

import dto.HitCreateRequest;
import dto.ViewStats;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dao.EndpointHitRepository;
import ru.practicum.ewm.mapper.EndpointHitMapper;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final EndpointHitMapper endpointHitMapper;
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void createHit(HitCreateRequest hitCreateRequest) {
        EndpointHit hit = endpointHitMapper.requestToEndpointHit(hitCreateRequest);
        endpointHitRepository.save(hit);
        log.info("EndpointHit saved");
    }

    @Override
    public Collection<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        List<ViewStats> viewStatsList = endpointHitRepository.findViewStatsByDateAndUri(start, end, uris);

        if (unique) {
            viewStatsList = viewStatsList.stream()
                    .map(stat -> {
                        long uniqueHits = endpointHitRepository.countDistinctByAppAndUriAndTimestampBetween(stat.getApp(),
                                stat.getUri(), start, end);
                        return new ViewStats(stat.getApp(), stat.getUri(), uniqueHits);
                    })
                    .toList();
        }
        log.info("Statistic Gathered");
        return viewStatsList;
    }
}
