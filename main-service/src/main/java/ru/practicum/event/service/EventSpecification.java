package ru.practicum.event.service;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.SearchFilter;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public class EventSpecification {
    public List<Specification<Event>> getSpecifications(SearchFilter filter) {
        List<Specification<Event>> specifications = new ArrayList<>();
        specifications.add(filter.getInitiators() == null ? null : initiatorIdIn(filter.getInitiators()));
        specifications.add(filter.getCategories() == null ? null : catIdsIn(filter.getCategories()));
        specifications.add(filter.getStates() == null ? null : statesIn(filter.getStates()));
        specifications.add(filter.getRangeStart() == null ? null : eventAfter(filter.getRangeStart()));
        specifications.add(filter.getRangeEnd() == null ? null : eventBefore(filter.getRangeEnd()));
        return specifications.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private Specification<Event> initiatorIdIn(List<Integer> ids) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("initiator").get("id")).value(ids);
    }

    private Specification<Event> statesIn(List<State> states) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("state")).value(states);
    }

    private Specification<Event> catIdsIn(List<Integer> ids) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("category").get("id")).value(ids);
    }

    private Specification<Event> annotationOrDescriptionLike(String text) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), text),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), text)
        );
    }

    private Specification<Event> eventAfter(LocalDateTime startRange) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startRange);
    }

    private Specification<Event> eventBefore(LocalDateTime endRange) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("eventDate"), endRange);
    }

    private Specification<Event> eventPaid(Boolean isPaid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("paid"), isPaid);
    }

    private Specification<Event> handleOnlyAvailable(Boolean onlyAvailable) {
        if (onlyAvailable) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("confirmedRequests"),
                    root.get("participantLimit"));
        }
        return null;
    }

    private Specification<Event> eventAfterNow() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
                LocalDateTime.now());
    }

    private Specification<Event> eventIsPublished() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("state"), State.PUBLISHED);
    }
}
