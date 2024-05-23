package ru.practicum.event.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchFilter {
    private List<Integer> initiators;
    private List<State> states;
    private String text;
    private List<Integer> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable = false;
}
