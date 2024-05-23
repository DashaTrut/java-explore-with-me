package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static ru.practicum.Constant.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private int confirmedRequests; //Количество одобренных заявок на участие в данном событии
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private int id;
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid; //Нужно ли оплачивать участие
    private int participantLimit; //default: 0 Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private LocalDateTime publishedOn;
    private Boolean requestModeration; //default: true Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    private State state;
    @NotNull
    private String title;
    private int views; //Количество просмотров события
}
