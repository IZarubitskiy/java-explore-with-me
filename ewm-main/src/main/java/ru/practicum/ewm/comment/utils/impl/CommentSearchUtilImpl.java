package ru.practicum.ewm.comment.utils.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.comment.dao.CommentRepository;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.utils.CommentSearchUtil;
import ru.practicum.ewm.exeption.exemptions.NotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentSearchUtilImpl implements CommentSearchUtil {

    private final CommentRepository commentRepository;

    @Override
    public Comment findCommentById(Long commentId) {
        log.info("Searching Comment with id={}", commentId);
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id=%d not found", commentId)));
    }
}
