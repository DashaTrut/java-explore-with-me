package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private int id;
    private Set<EventShortDto> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "Заголовок не может быть пустым или слишком большим")
    private String title;

}
