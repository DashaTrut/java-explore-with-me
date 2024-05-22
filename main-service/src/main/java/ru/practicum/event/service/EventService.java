package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.EndpointHit;
import ru.practicum.HttpClient;
import ru.practicum.ViewStats;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.errors.exception.BadException;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.errors.exception.ValidationException;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SearchFilter;
import ru.practicum.event.model.State;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.repository.EventRepositoryJpa;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepositoryJpa;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventSpecification eventSpecification = new EventSpecification();
    private final EventRepositoryJpa eventRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;
    private final CategoryService categoryService;
    private final HttpClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public List<EventFullDto> adminGetEventsByParameters(List<Integer> users, List<String> states, List<Integer> categories,
                                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        SearchFilter filter = new SearchFilter();
        filter.setInitiators(users);
        if (states != null) {
            List<State> stateList = new ArrayList<>();
            for (String state : states) {
                stateList.add(toStateByString(state));
            }
            filter.setStates(stateList);
        }
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(DESC, "id"));
        List<Specification<Event>> specifications = eventSpecification.adminGetSpecifications(filter);
        Page<Event> eventPage = eventRepositoryJpa.findAll(specifications.stream()
                .reduce(Specification::or)
                .orElse(null), pageable);
        if (!eventPage.hasContent()) {
            return new ArrayList<>();
        }

        return eventPage.getContent().stream()
                .map(EventMapper::toEventFullDto)
                .collect(toList());
    }

    @Transactional
    public EventFullDto adminUpdateEvent(Integer eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEvent(eventId);
        Boolean a = event.getEventDate().isAfter(LocalDateTime.now().plusHours(1));
        if (a) {
            if (updateEventAdminRequest.getEventDate() != null && updateEventAdminRequest.getEventDate().isAfter(LocalDateTime.now().plusHours(1))) {
                event.setEventDate(updateEventAdminRequest.getEventDate());
            }
        } else {
            throw new BadException("дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (event.getState().equals(State.PUBLISHED) && updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                throw new ValidationException("событие можно отклонить, только если оно еще не опубликовано");
            }
            if (!event.getState().equals(State.PENDING) && updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                throw new ValidationException("событие можно публиковать, только если оно в состоянии ожидания публикации");
            }
            event.setState(toState(updateEventAdminRequest.getStateAction()));
            event.setPublishedOn(LocalDateTime.now());
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryService.categoryExists(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        Event eventResponse = eventRepositoryJpa.save(event);
        return EventMapper.toEventFullDto(eventResponse);
    }

    @Transactional
    public List<EventShortDto> publicGetEventByParameters(String text, List<Integer> categories, Boolean paid,
                                                          LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                          Boolean onlyAvailable, String sort, int from, int size,
                                                          HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadException("Конец диапазона раньше начала диапазона");
        }
        SearchFilter filter = new SearchFilter();
        filter.setText(text);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        filter.setPaid(paid);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(DESC, "id"));
        List<Specification<Event>> specifications = eventSpecification.adminGetSpecifications(filter);
        Page<Event> eventPage = eventRepositoryJpa.findAll(specifications.stream()
                .reduce(Specification::or)
                .orElse(null), pageable);
        if (!eventPage.hasContent()) {
            return new ArrayList<>();
        }
        List<EventShortDto> eventShortDtoList = eventPage.getContent().stream()
                .map(EventMapper::toEventShortDto)
                .collect(toList());
        setViews(eventShortDtoList);
        addStatistic(request);
        return eventShortDtoList;
    }

    @Transactional
    public EventFullDto publicGetEventById(Integer id, HttpServletRequest request) {
        Event event = getEvent(id);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new EntityNotFoundException("событие должно быть опубликовано");
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setViews(getViewsForEvent(List.of(request.getRequestURI())));
        addStatistic(request);
        return eventFullDto;
    }

    @Transactional
    public EventFullDto createEvent(Integer userId, NewEventDto newEventDto) {
        User registrator = getUser(userId);
        Category category = categoryService.categoryExists(newEventDto.getCategory());
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("До начала события меньше двух часов");
        }
        Event eventToSave = EventMapper.toEvent(newEventDto, category, registrator);
        eventToSave.setState(State.PENDING);
        eventToSave.setCreatedOn(LocalDateTime.now());
        log.info("Мероприятие {} ", eventToSave);
        Event event = eventRepositoryJpa.save(eventToSave);
        return EventMapper.toEventFullDto(event);
    }

    public List<EventShortDto> privateEventsForUser(Integer userId, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by(ASC, "id"));
        Page<Event> eventPage = eventRepositoryJpa.findAllByInitiatorId(userId, pageable);
        if (!eventPage.hasContent()) {
            return new ArrayList<>();
        }
        return eventPage.getContent().stream()
                .map(EventMapper::toEventShortDto)
                .collect(toList());
    }

    public EventFullDto privateGetEventId(Integer userId, Integer eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException("Вы не организатор мероприятия");
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setViews(getViewsForEvent(List.of("event" + eventId)));
        return eventFullDto;
    }

    public User getUser(Integer userId) {
        return userRepositoryJpa.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Зарегистрированного пользователя не существует"));
    }

    @Transactional
    public EventFullDto privateUpdateEventByInitiator(Integer userId, Integer eventId, UpdateEventUserRequest updateEventUser) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("событие нельзя изменить, если оно опубликовано");
        } else if (updateEventUser.getStateAction() != null) {
            event.setState(toStateByString(updateEventUser.getStateAction()));
            event.setPublishedOn(LocalDateTime.now());
        }
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException("Вы не организатор мероприятия");
        }
        if (updateEventUser.getEventDate() != null) {
            if (updateEventUser.getEventDate().isAfter(LocalDateTime.now().plusHours(2))) {
                throw new ValidationException("До начала события меньше двух часов");
            } else {
                event.setEventDate(updateEventUser.getEventDate());
            }
        }
        if (updateEventUser.getAnnotation() != null) {
            event.setAnnotation(updateEventUser.getAnnotation());
        }
        if (updateEventUser.getCategory() != null) {
            event.setCategory(categoryService.categoryExists(updateEventUser.getCategory()));
        }
        if (updateEventUser.getDescription() != null) {
            event.setDescription(updateEventUser.getDescription());
        }
        if (updateEventUser.getLocation() != null) {
            event.setLocation(updateEventUser.getLocation());
        }
        if (updateEventUser.getPaid() != null) {
            event.setPaid(updateEventUser.getPaid());
        }
        if (updateEventUser.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUser.getParticipantLimit());
        }
        if (updateEventUser.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUser.getRequestModeration());
        }
        if (updateEventUser.getTitle() != null) {
            event.setTitle(updateEventUser.getTitle());
        }
        Event eventResponse = eventRepositoryJpa.save(event);
        return EventMapper.toEventFullDto(eventResponse);
    }


    private int getViewsForEvent(List<String> uris) {
        try {
            List<ViewStats> list = statsClient.getStatistic(LocalDateTime.of(2000, 1, 1, 1, 1, 1).format(formatter),
                    LocalDateTime.now().format(formatter), uris, true);
            if (list.isEmpty()) {
                return 0;
            }
            return list.get(0).getHits();

        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    private void setViews(List<EventShortDto> eventShortDtoList) {
        List<String> uris = new ArrayList<>();
        for (EventShortDto dto : eventShortDtoList) {
            uris.add("/events/" + dto.getId());
        }
        List<ViewStats> list = statsClient.getStatistic(
                LocalDateTime.of(2000, 1, 1, 1, 1, 1).format(formatter), LocalDateTime.now().format(formatter), uris, true);
        Map<Integer, EventShortDto> eventShortDtoMap = eventShortDtoList.stream()
                .collect(Collectors.toMap(EventShortDto::getId, Function.identity()));

        for (ViewStats dto : list) {
            Integer id = Integer.parseInt(dto.getUri().substring(8));
            eventShortDtoMap.get(id).setViews(dto.getHits());
        }
    }

    public void addStatistic(HttpServletRequest request) {
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main-service")
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter))
                .uri(request.getRequestURI())
                .build();
        statsClient.addStatistic(endpointHit);
    }

    public State toState(StateAction stateAction) {
        if (stateAction.equals(StateAction.REJECT_EVENT)) {
            return State.CANCELED;
        }
        return State.PUBLISHED;
    }

    public Event getEvent(Integer eventId) {
        return eventRepositoryJpa.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Мероприятия не существует"));
    }

    public static State toStateByString(String state) {
        try {
            if (state.equals("SEND_TO_REVIEW")) {
                return (State.PENDING);
            } else {
                return (State.CANCELED);
            }
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Неправильный state");
        }
    }
}
