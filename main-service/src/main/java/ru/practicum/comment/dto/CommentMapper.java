package ru.practicum.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public Comment toComment(NewCommentDto text, User user, Event event) {
        return Comment.builder()
                .text(text.getText())
                .event(event)
                .user(user)
                .created(LocalDateTime.now())
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .eventId(comment.getEvent().getId())
                .userId(comment.getUser().getId())
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> toListCommentDto(List<Comment> list) {
        return list.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
