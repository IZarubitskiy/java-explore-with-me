package ru.practicum.ewm.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.comment.dto.CommentCreateRequest;
import ru.practicum.ewm.comment.dto.CommentResponse;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

@Mapper
public interface CommentMapper {

    @Mapping(target = "event", source = "event")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentCreateRequest commentCreateDto, User author, Event event);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "eventId", source = "event.id")
    @Mapping(target = "updateDate", source = "comment.updateDate")
    CommentResponse toResponse(Comment comment);

}