package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.model.UnitStats;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class StatsMapper {
    public ViewStats toStatsResponse(UnitStats unitStats) {
        return ViewStats.builder()
                .app(unitStats.getApp())
                .uri(unitStats.getUri())
                //.hits(unitStats.getHits())
                .build();
    }

    public List<ViewStats> toListRequestResponseDto(List<UnitStats> unitStats) {
        List<ViewStats> result = new ArrayList<>();
        for (UnitStats request : unitStats) {
            result.add(toStatsResponse(request));
        }
        return result;
    }

    public UnitStats toViewStats(EndpointHit endpointHit) {
        return UnitStats.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .timestamp(endpointHit.getTimestamp())
                .ip(endpointHit.getIp())
                .build();
    }
}
