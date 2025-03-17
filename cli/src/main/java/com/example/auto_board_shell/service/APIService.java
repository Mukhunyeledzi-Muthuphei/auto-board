package com.example.auto_board_shell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.auto_board_shell.helpers.CurrentUser;

@Service
public class APIService {

    private final WebClient webClient;

    @Autowired
    public APIService(
            @Value("http://localhost:8080/api") String apiBaseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // Generic method for authenticated GET requests
    public <T> T get(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    // Generic method for authenticated POST requests
    public <T> T post(String uri, Object body, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, CurrentUser.getToken())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    // Generic method for authenticated PUT requests
    public <T> T put(String uri, Object body, Class<T> responseType) {
        return webClient.put()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, CurrentUser.getToken())
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    // Generic method for authenticated DELETE requests
    public <T> T delete(String uri, Class<T> responseType) {
        return webClient.delete()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, CurrentUser.getToken())
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }
}