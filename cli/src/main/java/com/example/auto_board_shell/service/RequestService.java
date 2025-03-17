package com.example.auto_board_shell.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Service
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cli.api.base-url}")
    private String baseUrl;

    private String buildUrl(String endpoint) {
        return baseUrl + endpoint;
    }

    public <T> APIResponse<T> get(String endpoint, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(buildUrl(endpoint), HttpMethod.GET, null, responseType);
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    public <T, R> APIResponse<T> post(String endpoint, R request, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    buildUrl(endpoint),
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    responseType
            );
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }


    public <T, R> APIResponse<T> put(String endpoint, R request, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(buildUrl(endpoint), HttpMethod.PUT, new HttpEntity<>(request), responseType);
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    // TODO this needs updating because we may need to parse data to see what we want to delete
    public APIResponse<Void> delete(String endpoint) {
        try {
            restTemplate.delete(buildUrl(endpoint));
            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Deleted successfully");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage(), null);
        }
    }
}
