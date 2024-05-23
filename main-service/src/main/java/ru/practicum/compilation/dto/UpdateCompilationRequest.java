package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private List<Integer> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Имя не может быть пустым или слишком большим")
    private String title;
}
