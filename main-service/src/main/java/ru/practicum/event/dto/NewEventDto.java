package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.Constant.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация не может быть пустым или слишком большим")
    private String annotation;
    @NotNull
    @Positive
    private int category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание не может быть пустым или слишком большим")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Future
    private LocalDateTime eventDate; //Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @NotNull
    private Location location;
    private Boolean paid = false; //default: false Нужно ли оплачивать участие в событии
    @PositiveOrZero
    private Integer participantLimit = 0;//default: 0 Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration = true; //default: true Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    @NotBlank
    @Size(min = 3, max = 120, message = "Описание не может быть пустым или слишком большим")
    private String title;
}
