package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class HttpClient extends BaseClient {

    @Autowired
    public HttpClient(@Value("${stats-server.url}") String url, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addStatistic(EndpointHit endpointHit) {
        return post("/hit", endpointHit);
    }

    public List<ViewStats> getStatistic(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", start,
                "end", end,
                "unique", unique
        );
        StringBuilder path = new StringBuilder("/stats?start={start}&end={end}");
        for (String uri : uris) {
            path.append("&uris=").append(uri);
        }
        path.append("&unique={unique}");
        return getForList(path.toString(), params);
    }
}

