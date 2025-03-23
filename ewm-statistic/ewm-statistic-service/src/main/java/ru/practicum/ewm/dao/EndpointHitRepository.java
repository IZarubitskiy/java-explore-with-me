package ru.practicum.ewm.dao;

import dto.ViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.StatsDto(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN :startTime AND :endTime " +
            "AND (:uris IS NULL OR e.uri IN :uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<ViewStats> findViewStatsByDateAndUri(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("uris") List<String> uris);

    @Query("SELECT COUNT(DISTINCT e.ip) " +
            "FROM EndpointHit e " +
            "WHERE e.app = :app " +
            "AND e.uri = :uri " +
            "AND e.timestamp BETWEEN :startTime AND :endTime")
    long countDistinctByAppAndUriAndTimestampBetween(@Param("app") String app,
                                                     @Param("uri") String uri,
                                                     @Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);
}
