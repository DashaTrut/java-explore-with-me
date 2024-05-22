package ru.practicum.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@AllArgsConstructor
public class ApiError {
    private List<String> errors;
    private String message;
    private String reason;
    private Response status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
