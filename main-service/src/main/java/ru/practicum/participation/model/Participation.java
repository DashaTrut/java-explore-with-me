package ru.practicum.participation.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "participation")
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @CreationTimestamp
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester; //Идентификатор пользователя, отправившего заявку
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participation that = (Participation) o;
        return getEvent() == that.getEvent() && getId() == that.getId() && getRequester() == that.getRequester() &&
                Objects.equals(getCreated(), that.getCreated()) && Objects.equals(getStatus(), that.getStatus());
    }
}
