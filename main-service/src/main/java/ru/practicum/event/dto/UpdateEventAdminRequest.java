package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.StateAction;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.Constant.DATE_TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000, message = "аннотация не может быть пустым или слишком большим")
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000, message = "описание не может быть пустым или слишком большим")
    private String description;
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid; //Нужно ли оплачивать участие
    private Integer participantLimit;
    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    private StateAction stateAction; //Новое состояние события
    @Size(min = 3, max = 120, message = "заголовок не может быть пустым или слишком большим")
    private String title;
}
