package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.participation.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participation.dto.EventRequestStatusUpdateResult;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.service.ParticipationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationService participationService;
    private final CommentService commentService;


    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Integer userId,
                                 @PathVariable Integer eventId,
                                 @RequestBody @Valid NewCommentDto text) {
        return commentService.createComment(userId, eventId, text);

    }

    @PatchMapping("/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Integer userId,
                                    @PathVariable Integer commentId,
                                    @RequestBody @Valid NewCommentDto text) {
        return commentService.updateComment(userId, commentId, text);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        commentService.deleteComment(userId, commentId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByInitiator(@PathVariable @Positive Integer userId,
                                               @PathVariable @Positive Integer eventId,
                                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return eventService.privateUpdateEventByInitiator(userId, eventId, updateEventUserRequest);
    }

    @GetMapping
    public List<EventShortDto> getEventsForUser(@PathVariable @Positive Integer userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Мероприятия организованные пользователем {}", userId);
        return eventService.privateEventsForUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserByEventId(@PathVariable @Positive Integer userId, @PathVariable @Positive Integer eventId) {
        log.info("Мероприятие {} организованное пользователем {}", eventId, userId);
        return eventService.privateGetEventId(userId, eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable @Positive Integer userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.info("Создание мероприятие");
        return eventService.createEvent(userId, newEventDto);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable @Positive Integer userId,
                                                              @PathVariable @Positive Integer eventId) {
        return participationService.privateAllByInitiator(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable @Positive Integer userId,
                                                              @PathVariable @Positive Integer eventId,
                                                              @RequestBody @Valid
                                                              EventRequestStatusUpdateRequest updateRequest) {
        return participationService.privateUpdateRequestStatus(userId, eventId, updateRequest);
    }

}
