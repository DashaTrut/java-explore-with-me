package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepositoryJpa;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.event.service.EventService;
import ru.practicum.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepositoryJpa commentRepository;
    private final EventService eventService;
    private final UserService userService;

    public CommentDto createComment(Integer userId, Integer eventId, NewCommentDto text) {
        var event = eventService.getEvent(eventId);
        var user = userService.getUser(userId);
        Comment comment = commentRepository.save(CommentMapper.toComment(text, user, event));


        return CommentMapper.toCommentDto(comment);
    }

    public CommentDto updateComment(int userId, int commentId, NewCommentDto text) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException("Этого комментария не существует"));
        if (comment.getUser().getId() == (userId)) {
            comment.setText(text.getText());
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new EntityNotFoundException("Вы не создатель комментария");
        }
    }

    @Transactional
    public void deleteComment(int userId, int commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException("Зарегистрированного комментария не существует"));
        if (comment.getUser().getId() == (userId)) {
            commentRepository.deleteById(userId);
        } else {
            throw new EntityNotFoundException("Вы не создатель комментария");
        }
    }

    public void adminDeleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getCommentsByEvent(Integer eventId) {
        return CommentMapper.toListCommentDto(commentRepository.findByEventId(eventId));
    }

}

