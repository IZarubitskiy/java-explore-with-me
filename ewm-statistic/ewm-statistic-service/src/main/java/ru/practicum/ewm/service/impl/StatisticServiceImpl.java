package ru.practicum.ewm.service.impl;

import dto.HitCreateRequest;
import dto.ViewStats;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
    private final EntityManager entityManager;

    @Override
    public void createHit(HitCreateRequest hitCreateRequest) {
        EndpointHit hit = endpointHitMapper.requestToEndpointHit(hitCreateRequest);
        endpointHitRepository.save(hit);
        log.info("Hit saved");
    }

    @Override
    public Collection<ViewStats> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date should not be after end date");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<EndpointHit> hit = query.from(EndpointHit.class);

        Predicate whereClause = cb.between(hit.get("timestamp"), start.minusNanos(start.getNano()), end);

        if (uris != null && !uris.isEmpty()) {
            whereClause = cb.and(whereClause, hit.get("uri").in(uris));
        }

        if (unique) {
            query.multiselect(
                    cb.countDistinct(hit.get("ip")),
                    hit.get("app"),
                    hit.get("uri")
            );
        } else {
            query.multiselect(
                    cb.count(hit.get("ip")),
                    hit.get("app"),
                    hit.get("uri")
            );
        }

        query.where(whereClause)
                .groupBy(hit.get("app"), hit.get("uri"))
                .orderBy(cb.desc(cb.count(hit.get("ip"))));

        List<Tuple> tuples = entityManager.createQuery(query).getResultList();

        log.info("Returned list of getResponses");
        return tuples.stream().map(tuple ->
                ViewStats.builder()
                        .hits(tuple.get(0, Long.class))
                        .app(tuple.get(1, String.class))
                        .uri(tuple.get(2, String.class))
                        .build()
        ).toList();
    }
}
