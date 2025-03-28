package ru.practicum.ewm.event.utils.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.event.dao.LocationRepository;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.utils.LocationSearchUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocationSearchUtilImpl implements LocationSearchUtil {
    private final LocationRepository locationRepository;

    @Override
    public Location findById(Float lat, Float lon) {
        log.info("Searching location with lat = {} and lon = {}", lat, lon);
        return locationRepository
                .findByLatAndLon(lat, lon)
                .orElseGet(() -> locationRepository.save(Location.builder().lat(lat).lon(lon).build()));
    }
}