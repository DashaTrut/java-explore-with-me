package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.server.StatsServer;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {


    private final StatsServer statsServer;

    @PostMapping("/hit")
    public void addStats(@RequestBody EndpointHit endpointHit) {
        statsServer.addStats(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> searchStats(@Valid @RequestParam("start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @Valid @RequestParam("end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) List<String> uris, @RequestParam(required = false) boolean unique) {
        return statsServer.searchStats(start, end, uris, unique);
    }

}
