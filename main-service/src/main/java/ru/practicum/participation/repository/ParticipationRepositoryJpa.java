package ru.practicum.participation.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.participation.model.Participation;

import java.util.List;

public interface ParticipationRepositoryJpa extends JpaRepository<Participation, Integer> {
    Participation findByRequesterIdAndEventId(Integer requesterId, Integer eventId);

    List<Participation> findByRequesterId(Integer requesterId);

    List<Participation> findAllByEventId(Integer eventId, Sort sort);
}
