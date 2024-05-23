package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepositoryJpa;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepositoryJpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationService {
    private final CompilationRepositoryJpa compilationRepositoryJpa;
    private final EventRepositoryJpa eventRepositoryJpa;

    @Transactional
    public CompilationDto compilationCreateAdmin(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepositoryJpa.findAllByIdIn(newCompilationDto.getEvents());
        }
        Compilation compilation = compilationRepositoryJpa.save(CompilationMapper.toCompilation(newCompilationDto, events));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Transactional
    public CompilationDto compilationUpdateAdmin(UpdateCompilationRequest newCompilationDto, Integer compId) {
        Compilation compilation = getCompilation(compId);
        Set<Event> events;
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            events = eventRepositoryJpa.findAllByIdIn(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        Compilation compilationResponse = compilationRepositoryJpa.save(compilation);
        return CompilationMapper.toCompilationDto(compilationResponse);
    }

    public Compilation getCompilation(Integer compId) {
        return compilationRepositoryJpa.findById(compId).orElseThrow(() ->
                new EntityNotFoundException("Подборки не существует"));
    }

    @Transactional
    public void deleteCompilationAdmin(Integer compId) {
        getCompilation(compId);
        compilationRepositoryJpa.deleteById(compId);
    }

    public CompilationDto compilationGet(Integer compId) {
        Compilation compilation = compilationRepositoryJpa.findById(compId).orElseThrow(() ->
                new EntityNotFoundException("Подборки не существует"));
        return CompilationMapper.toCompilationDto(compilation);
    }

    public List<CompilationDto> compilationGetAll(Boolean pinned, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(ASC, "id"));
        Page<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepositoryJpa.findAll(pageable);
        } else {
            compilations = compilationRepositoryJpa.findAllByPinned(pinned, pageable);
        }
        if (!compilations.hasContent()) {
            return new ArrayList<>();
        }
        return compilations.getContent().stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
