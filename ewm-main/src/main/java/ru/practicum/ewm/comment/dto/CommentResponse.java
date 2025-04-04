package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.practicum.ewm.config.EWM_CONSTANTS.JSON_DATE_PATTERN;

public record CommentResponse(
        Long id,
        String text,
        Long eventId,
        Long authorId,
        @JsonFormat(pattern = JSON_DATE_PATTERN)
        LocalDateTime createDate,
        @JsonFormat(pattern = JSON_DATE_PATTERN)
        LocalDateTime updateDate) {
}
