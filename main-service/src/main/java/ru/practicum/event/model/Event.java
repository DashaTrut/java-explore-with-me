package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events", schema = "public")
public class Event {
    @Column(nullable = false, length = 2000)
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private int confirmedRequests; //Количество одобренных заявок на участие в данном событии
    @Column(name = "created")
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    //@ToString.Exclude
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Embedded
    private Location location;
    @Column(columnDefinition = "boolean default false")
    private Boolean paid; //Нужно ли оплачивать участие
    @Column(name = "participant_limit")
    private Integer participantLimit; //default: 0 Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    @Column(name = "request_moderation")
    private Boolean requestModeration; //default: true Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
    @Enumerated(EnumType.STRING)
    private State state;
    @NotNull
    private String title;
}
