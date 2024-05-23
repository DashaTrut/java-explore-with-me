package ru.practicum.participation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.errors.exception.EntityNotFoundException;
import ru.practicum.errors.exception.ValidationException;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepositoryJpa;
import ru.practicum.participation.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participation.dto.EventRequestStatusUpdateResult;
import ru.practicum.participation.dto.ParticipationMapper;
import ru.practicum.participation.dto.ParticipationRequestDto;
import ru.practicum.participation.model.Participation;
import ru.practicum.participation.model.ParticipationStatus;
import ru.practicum.participation.repository.ParticipationRepositoryJpa;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepositoryJpa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationService {
    private final ParticipationRepositoryJpa participationRepositoryJpa;
    private final EventRepositoryJpa eventRepositoryJpa;
    private final UserRepositoryJpa userRepositoryJpa;

    @Transactional
    public ParticipationRequestDto participationCreate(int userId, int eventId) {
        Participation participation = participationRepositoryJpa.findByRequesterIdAndEventId(userId, eventId);
        if (participation != null) {
            throw new ValidationException("У вас уже есть регистрация на это мероприятие");
        }
        Event event = getEvent(eventId);
        User user = getUser(userId);
        if (event.getInitiator().getId() == (user.getId())) {
            throw new ValidationException("Создатель события не может подать заявку на участие");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Событие не опубликовано");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ValidationException("Нет свободных мест на событие");
        }
        Participation p = ParticipationMapper.toParticipation(event, user);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            p.setStatus(ParticipationStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepositoryJpa.save(event);
        } else {
            p.setStatus(ParticipationStatus.PENDING);
        }
        Participation participationSave = participationRepositoryJpa.save(p);
        return ParticipationMapper.toParticipationRequestDto(participationSave);
    }

    public List<ParticipationRequestDto> participationGetForUser(Integer userId) {
        getUser(userId);
        List<Participation> list = participationRepositoryJpa.findByRequesterId(userId);
        return ParticipationMapper.tListParticipationRequestDto(list);
    }

    @Transactional
    public ParticipationRequestDto cancelParticipation(int userId, int requestId) {
        Participation participation = participationGet(requestId);
        if (participation.getRequester().getId() != userId) {
            throw new EntityNotFoundException("Вы не можете изменить чужую заявку");
        }
        if (participation.getStatus().equals(ParticipationStatus.CONFIRMED)) {
            Event event = getEvent(participation.getEvent().getId());
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepositoryJpa.save(event);
        }
        participation.setStatus(ParticipationStatus.CANCELED);
        return ParticipationMapper.toParticipationRequestDto(participationRepositoryJpa.save(participation));
    }

    public Participation participationGet(Integer id) {
        return participationRepositoryJpa.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Заявки не существует"));
    }

    public Event getEvent(Integer eventId) {
        return eventRepositoryJpa.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException("Мероприятия не существует"));
    }

    public User getUser(Integer userId) {
        return userRepositoryJpa.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователя не существует"));
    }

    public void checkInitiator(Integer userId, Event event) {
        if (event.getInitiator().getId().equals(userId)) {
            return;
        }
        throw new EntityNotFoundException("Вы не создатель мероприятия");
    }

    @Transactional
    public EventRequestStatusUpdateResult privateUpdateRequestStatus(Integer userId, Integer eventId,
                                                                     EventRequestStatusUpdateRequest updateRequest) {

        Event event = getEvent(eventId);
        checkInitiator(userId, event);
        if (event.getParticipantLimit().equals(0) || !event.getRequestModeration()) {
            throw new ValidationException("Подтверждение заявок не требуется");
        }
        List<Participation> requests = participationRepositoryJpa.findAllById(updateRequest.getRequestIds());
        int limit = event.getParticipantLimit();
        int confirmed = event.getConfirmedRequests();
        if (limit == confirmed) {
            throw new ValidationException("Заявки больше не принимаются");
        }
        Iterator<Participation> iterator = requests.iterator();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        while (limit > confirmed && iterator.hasNext()) {
            Participation request = iterator.next();
            if (!request.getStatus().equals(ParticipationStatus.PENDING)) {
                throw new ValidationException("Заявку уже рассмотрели");
            }
            request.setStatus(updateRequest.getStatus());


            if (updateRequest.getStatus().equals(ParticipationStatus.CONFIRMED)) {
                confirmedRequests.add(ParticipationMapper.toParticipationRequestDto(request));
                confirmed++;
            } else {
                rejectedRequests.add(ParticipationMapper.toParticipationRequestDto(request));
            }
        }
        while (iterator.hasNext()) {
            Participation request = iterator.next();
            request.setStatus(ParticipationStatus.REJECTED);
            rejectedRequests.add(ParticipationMapper.toParticipationRequestDto(request));
        }
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        event.setConfirmedRequests(confirmed);
        participationRepositoryJpa.saveAll(requests);
        eventRepositoryJpa.save(event);
        return result;
    }

    public List<ParticipationRequestDto> privateAllByInitiator(Integer userId, Integer eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        checkInitiator(userId, event);
        List<Participation> participations = participationRepositoryJpa.findAllByEventId(eventId, Sort.by(Sort.Direction.ASC, "id"));
        return ParticipationMapper.tListParticipationRequestDto(participations);
    }
}
