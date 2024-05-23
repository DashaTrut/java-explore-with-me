package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Set;

public interface EventRepositoryJpa extends JpaRepository<Event, Integer>, JpaSpecificationExecutor<Event> {

    Set<Event> findAllByIdIn(List<Integer> ids);

    Page<Event> findAllByInitiatorId(Integer userId, Pageable pageable);
}
