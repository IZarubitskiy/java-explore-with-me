package ru.practicum.ewm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHit;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
}
