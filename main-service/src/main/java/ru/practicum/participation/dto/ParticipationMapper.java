package ru.practicum.participation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.participation.model.Participation;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ParticipationMapper {
    public Participation toParticipation(Event event, User user) {
        return Participation.builder()
                .requester(user)
                .event(event)
                .build();
    }

    public ParticipationRequestDto toParticipationRequestDto(Participation participation) {
        return ParticipationRequestDto.builder()
                .id(participation.getId())
                .created(participation.getCreated())
                .requester(participation.getRequester().getId())
                .event(participation.getEvent().getId())
                .status(participation.getStatus().toString())
                .build();
    }

    public List<ParticipationRequestDto> tListParticipationRequestDto(List<Participation> participations) {
        return participations.stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
