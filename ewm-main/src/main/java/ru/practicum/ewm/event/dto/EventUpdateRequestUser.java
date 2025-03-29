package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.enums.EventStateAction;
import ru.practicum.ewm.event.validation.EventDateTime;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventUpdateRequestUser {
    @Size(min = 20, max = 2000, message = "Annotation should be between 20 and 2000 characters long")
    String annotation;

    @Size(min = 20, max = 7000, message = "Description should be between 20 and 7000 characters long")
    String description;

    @EventDateTime
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @Size(min = 3, max = 120, message = "Title should be between 3 and 120 characters long")
    String title;

    Long category;
    Location location;
    Boolean paid;

    @PositiveOrZero
    Integer participantLimit;

    Boolean requestModeration;
    EventStateAction stateAction;
}