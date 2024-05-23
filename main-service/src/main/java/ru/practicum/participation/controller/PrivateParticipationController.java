package ru.practicum.participation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.service.ParticipationService;

import javax.validation.constraints.Positive;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateParticipationController {
    private final ParticipationService participationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto participationCreate(@PathVariable @Positive Integer userId,
                                                       @RequestParam @Positive Integer eventId) {
        log.info("Creating participation {} for event {}", userId, eventId);
        return participationService.participationCreate(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipation(@PathVariable @Positive int userId) {
        log.info("Get all user");
        return participationService.participationGetForUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto updateParticipation(@PathVariable @Positive Integer userId,
                                                       @PathVariable @Positive Integer requestId) {
        log.info("Update participation {}", userId);
        return participationService.cancelParticipation(userId, requestId);
    }
}
