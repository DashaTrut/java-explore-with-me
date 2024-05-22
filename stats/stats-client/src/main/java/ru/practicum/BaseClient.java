package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BaseClient {
    protected final RestTemplate rest;

    public BaseClient(RestTemplate rest) {
        this.rest = rest;
    }

    private static <R> ResponseEntity<R> prepareResponse(ResponseEntity<R> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return sendRequest(HttpMethod.GET, path, parameters, null, Object.class);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return sendRequest(HttpMethod.POST, path, null, body, Object.class);
    }

    protected List<ViewStats> getForList(String path, @Nullable Map<String, Object> parameters) {
        ResponseEntity<List<ViewStats>> response = sendRequestForList(HttpMethod.GET, path, parameters, null,
                new ParameterizedTypeReference<List<ViewStats>>() {
                });
        return response.getBody();
    }

    private <T, R> ResponseEntity<R> sendRequest(HttpMethod method, String path,
                                                 @Nullable Map<String, Object> parameters, @Nullable T body,
                                                 Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, createHeadersDefault());
        ResponseEntity<R> response;
        try {
            if (parameters != null) {
                response = rest.exchange(path, method, requestEntity, responseType, parameters);
            } else {
                response = rest.exchange(path, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
        return prepareResponse(response);
    }

    private <T> ResponseEntity<List<ViewStats>> sendRequestForList(HttpMethod method, String path,
                                                                   @Nullable Map<String, Object> parameters, @Nullable T body,
                                                                   ParameterizedTypeReference<List<ViewStats>> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, createHeadersDefault());
        ResponseEntity<List<ViewStats>> response;
        try {
            if (parameters != null) {
                response = rest.exchange(path, method, requestEntity, responseType, parameters);
            } else {
                response = rest.exchange(path, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Collections.emptyList());
        }
        return prepareResponse(response);
    }

    private HttpHeaders createHeadersDefault() {
        HttpHeaders defaultHeaders = new HttpHeaders();
        defaultHeaders.setContentType(MediaType.APPLICATION_JSON);
        defaultHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return defaultHeaders;
    }
}