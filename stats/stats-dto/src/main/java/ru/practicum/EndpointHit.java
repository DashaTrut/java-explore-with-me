package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    @Null
    private int id;
    @NotBlank(message = "App не может быть пустым")
    private String app;
    @NotBlank(message = "URL не может быть пустым")
    private String uri;
    @NotBlank(message = "Ip не может быть пустым")
    private String ip;
    @NotNull(message = "Время запроса не может быть пустым")
    private String timestamp;

    @Override
    public String toString() {
        return "EndpointHit{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
