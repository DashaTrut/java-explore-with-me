package ru.practicum.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Set<EventShortDto> toSetEventShortDto(Set<Event> event) {
        return event.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toSet());
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .state(event.getState())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Event toEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .location(newEventDto.getLocation())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .paid(newEventDto.getPaid())
                .title(newEventDto.getTitle())
                .build();
    }

}
