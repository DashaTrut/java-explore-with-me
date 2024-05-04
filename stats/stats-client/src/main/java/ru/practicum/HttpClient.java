package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HttpClient extends BaseClient {

    @Autowired
    public HttpClient(String url, RestTemplateBuilder builder) {
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

    public ResponseEntity<Object> getStatistic(String startDate, String endDate, List<String> uris, Boolean unique) {
        Map<String, Object> params = Map.of(
                "start", startDate,
                "end", endDate,
                "uris", String.join(",", uris),
                "unique", unique
        );
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", params);
    }
}


