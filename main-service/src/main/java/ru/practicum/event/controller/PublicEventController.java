package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.Constant.DATE_TIME_FORMAT;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                         LocalDateTime rangeStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                         LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size,
                                         HttpServletRequest request) {
        return eventService.publicGetEventByParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request);
    }


    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable @Positive Integer id, HttpServletRequest request) {
        return eventService.publicGetEventById(id, request);
    }
}

