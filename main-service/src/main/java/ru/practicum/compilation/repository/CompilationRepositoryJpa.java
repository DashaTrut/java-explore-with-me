package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

public interface CompilationRepositoryJpa extends JpaRepository<Compilation, Integer> {

    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
