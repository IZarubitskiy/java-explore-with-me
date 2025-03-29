package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EventGetRequestAdmin {
    Set<Long> users;
    Set<String> states;
    Set<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Integer from;
    Integer size;
}
