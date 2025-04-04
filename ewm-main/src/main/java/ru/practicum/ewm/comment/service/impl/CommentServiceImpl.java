package ru.practicum.ewm.comment.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dao.CommentRepository;
import ru.practicum.ewm.comment.dto.CommentCreateRequest;
import ru.practicum.ewm.comment.dto.CommentResponse;
import ru.practicum.ewm.comment.dto.CommentUpdateRequest;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.service.CommentService;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.enums.EventState;
import ru.practicum.ewm.event.utils.EventSearchUtil;
import ru.practicum.ewm.exeption.exemptions.GetPublicEventException;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    final EventSearchUtil eventSearchUtil;
    final CommentMapper commentMapper;
    final CommentRepository commentRepository;
    final UserService userService;

    @Override
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest, Long userId, Long eventId) {
        Event event = eventSearchUtil.findById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new GetPublicEventException("Event must be published");
        }

        User author = userService.getUserById(userId);

        Comment comment = commentMapper.toComment(commentCreateRequest, author, event);
        comment.setCreateDate(LocalDateTime.now());
        log.info("Create new comment for eventId={} with userId={}", eventId, userId);
        return commentMapper.toResponse(commentRepository.save(comment));

    }

    @Override
    public List<CommentResponse> getCommentsByEventId(Long eventId, Integer from, Integer size) {
        Pageable pageRequest = PageRequest.of(from / size, size);
        Page<Comment> commentPage = commentRepository.findByEventId(eventId, pageRequest);

        return commentPage.getContent()
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = findCommentById(commentId);
        Event event = eventSearchUtil.findById(comment.getEvent().getId());

        return commentMapper.toResponse(comment);
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
        log.info("Comment with id={} was deleted by Administrator", commentId);
    }

    @Override
    public CommentResponse updateCommentByUser(Long commentId, Long userId, Long eventId, CommentUpdateRequest commentUpdateRequest) {
        Comment comment = findCommentById(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("The comment Author does not match the user id");
        }


        if (!comment.getEvent().getId().equals(eventId)) {
            throw new NotFoundException(String.format("Event don't have comment with id=%d", commentId));
        }

        comment.setText(commentUpdateRequest.text());
        comment.setUpdateDate(LocalDateTime.now());

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Long commentId, Long userId) {
        Comment comment = findCommentById(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("The comment Author does not match the user id");
        }

        commentRepository.deleteById(commentId);
        log.info("Comment with id={} was deleted by User", userId);
    }

    public Comment findCommentById(Long commentId) {
        log.info("Searching Comment with id={}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d not found", commentId)));
    }
}


