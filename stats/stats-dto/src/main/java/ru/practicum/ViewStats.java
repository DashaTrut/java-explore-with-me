package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {

    private String app;
    private String uri;
    private int hits;


    @Override
    public String toString() {
        return "StatsResponse{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }

    public ViewStats(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = (int) hits;
    }

}
