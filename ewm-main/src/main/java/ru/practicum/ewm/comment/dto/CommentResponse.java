package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.practicum.ewm.config.ewmMainCosnstants.JsonDatePattern;

public record CommentResponse(
        Long id,
        String text,
        Long eventId,
        Long authorId,
        @JsonFormat(pattern = JsonDatePattern)
        LocalDateTime createDate,
        @JsonFormat(pattern = JsonDatePattern)
        LocalDateTime updateDate) {
}
