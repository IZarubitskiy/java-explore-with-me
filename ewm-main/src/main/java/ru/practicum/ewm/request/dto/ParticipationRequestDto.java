package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.model.enums.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.ewm.config.Constants.JSON_DATE_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    @JsonFormat(pattern = JSON_DATE_PATTERN)
    LocalDateTime created;

    Long event;
    Long id;
    Long requester;
    RequestStatus status;
}