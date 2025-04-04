package ru.practicum.ewm.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import static ru.practicum.ewm.config.ewmMainCosnstants.JsonDatePattern;

public record CommentUpdateRequest(
        @NotBlank
        @Size(min = 20, max = 2000)
        String text,
        @JsonFormat(pattern = JsonDatePattern)
        LocalDateTime updateDate) {}
