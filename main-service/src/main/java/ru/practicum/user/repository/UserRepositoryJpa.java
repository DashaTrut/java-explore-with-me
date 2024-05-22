package ru.practicum.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserRepositoryJpa extends JpaRepository<User, Integer> {

    Page<User> findByIdIn(List<Integer> ids, Pageable pageable);
}