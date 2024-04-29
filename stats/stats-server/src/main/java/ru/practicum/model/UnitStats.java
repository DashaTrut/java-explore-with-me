package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "stats", schema = "public")
public class UnitStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitStats unitStats = (UnitStats) o;
        return getId() == unitStats.getId() && Objects.equals(getApp(), unitStats.getApp()) && Objects.equals(getUri(), unitStats.getUri()) && Objects.equals(getIp(), unitStats.getIp()) && Objects.equals(getTimestamp(), unitStats.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getApp(), getUri(), getIp(), getTimestamp());
    }
}
