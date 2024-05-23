package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepositoryJpa extends JpaRepository<Comment, Integer> {
    List<Comment> findByEventId(Integer eventId);

    List<Comment> findByUserId(Integer userId);
}
