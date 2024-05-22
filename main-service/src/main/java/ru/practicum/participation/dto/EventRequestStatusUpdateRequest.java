package ru.practicum.participation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.participation.model.ParticipationStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    //Изменение статуса запроса на участие в событии текущего пользователя
    private List<Integer> requestIds; //Идентификаторы запросов на участие в событии текущего пользователя

    private ParticipationStatus status;
}
