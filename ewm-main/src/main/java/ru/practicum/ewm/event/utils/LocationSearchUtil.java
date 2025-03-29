package ru.practicum.ewm.event.utils;

import ru.practicum.ewm.event.model.Location;

public interface LocationSearchUtil {
    Location findById(Float lat, Float lon);
}
