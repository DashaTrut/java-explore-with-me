package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHit;
import ru.practicum.model.UnitStats;

@UtilityClass
public class StatsMapper {

    public UnitStats toViewStats(EndpointHit endpointHit) {
        return UnitStats.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp())
                .ip(endpointHit.getIp())
                .build();
    }
}
