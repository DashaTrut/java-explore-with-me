package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHit;
import ru.practicum.model.UnitStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class StatsMapper {

    public UnitStats toViewStats(EndpointHit endpointHit) {
        return UnitStats.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .timestamp(LocalDateTime.parse(endpointHit.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .ip(endpointHit.getIp())
                .build();
    }
}
