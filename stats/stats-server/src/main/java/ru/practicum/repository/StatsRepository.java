package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStats;
import ru.practicum.model.UnitStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<UnitStats, Integer> {

    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM UnitStats s " +
            "WHERE s.uri IN ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIpUrisIn(List<String> uris, LocalDateTime start, LocalDateTime end);


    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM UnitStats s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStats> findAllStatsUniqueIp(LocalDateTime start, LocalDateTime end);


    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM UnitStats s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findAllStatsIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM UnitStats s " +
            "WHERE s.uri IN ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStats> findAllStatsUrisIn(List<String> uris, LocalDateTime start, LocalDateTime end);


}