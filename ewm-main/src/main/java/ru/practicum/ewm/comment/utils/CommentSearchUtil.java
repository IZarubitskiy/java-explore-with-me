package ru.practicum.ewm.comment.utils;

import ru.practicum.ewm.comment.model.Comment;

public interface CommentSearchUtil {

    Comment findCommentById(Long commentId);
}
