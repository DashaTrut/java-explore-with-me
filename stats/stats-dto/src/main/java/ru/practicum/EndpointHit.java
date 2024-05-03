package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {

    @NotBlank(message = "App не может быть пустым")
    private String app;

    @NotBlank(message = "URL не может быть пустым")
    private String uri;

    @NotBlank(message = "Ip не может быть пустым")
    private String ip;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Время запроса не может быть пустым")
    private LocalDateTime timestamp;
}
