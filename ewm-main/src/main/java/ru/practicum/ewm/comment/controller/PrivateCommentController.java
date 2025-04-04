package ru.practicum.ewm.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentCreateRequest;
import ru.practicum.ewm.comment.dto.CommentResponse;
import ru.practicum.ewm.comment.dto.CommentUpdateRequest;
import ru.practicum.ewm.comment.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(@Valid @RequestBody CommentCreateRequest commentCreateRequest,
                                         @PathVariable Long userId,
                                         @PathVariable Long eventId) {
        return commentService.createComment(commentCreateRequest, userId, eventId);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentResponse updateComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return commentService.updateCommentByUser(commentId, userId, eventId, commentUpdateRequest);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long userId,
            @PathVariable Long commentId) {
        commentService.deleteCommentByUser(commentId, userId);
    }
}
