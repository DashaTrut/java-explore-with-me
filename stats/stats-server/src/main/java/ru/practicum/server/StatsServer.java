package ru.practicum.server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.errors.exception.BadException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.model.UnitStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatsServer {
    private final StatsRepository statsRepository;


    public void addStats(EndpointHit endpointHit) {
        UnitStats stats = StatsMapper.toViewStats(endpointHit);
        statsRepository.save(stats);
    }


    public List<ViewStats> searchStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new BadException("Конец диапазона раньше начала диапазона");
        }
        if (uris == null) {
            if (unique == null || unique == false) {
                return statsRepository.findAllStatsIp(start, end);
            } else {
                return statsRepository.findAllStatsUniqueIp(start, end);
            }
        }
        if (unique) {
            return statsRepository.findAllStatsUniqueIpUrisIn(uris, start, end);
        } else {
            return statsRepository.findAllStatsUrisIn(uris, start, end);
        }
    }

}

