package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentCreateRequest;
import ru.practicum.ewm.comment.dto.CommentResponse;
import ru.practicum.ewm.comment.dto.CommentUpdateRequest;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(CommentCreateRequest commentCreateRequest, Long userId, Long eventId);

    List<CommentResponse> getCommentsByEventId(Long eventId, Integer from, Integer size);

    CommentResponse getCommentById(Long commentId);

    void deleteCommentByAdmin(Long commentId);

    CommentResponse updateCommentByUser(Long commentId, Long userId, Long eventId, CommentUpdateRequest updateDto);

    void deleteCommentByUser(Long commentId, Long userId);
}
