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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.example.auto_board_shell.helpers.CurrentUser;

@Service
public class RequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${cli.api.base-url}")
    private String baseUrl;

    private String buildUrl(String endpoint) {
        return baseUrl + endpoint;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, CurrentUser.getToken());
        return headers;
    }

    public <T> APIResponse<T> get(String endpoint, ParameterizedTypeReference<T> responseType) {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
            ResponseEntity<T> response = restTemplate.exchange(buildUrl(endpoint), HttpMethod.GET, entity,
                    responseType);
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    public <T, R> APIResponse<T> post(String endpoint, R request, ParameterizedTypeReference<T> responseType) {
        try {
            HttpEntity<R> entity = new HttpEntity<>(request, createHeaders());
            ResponseEntity<T> response = restTemplate.exchange(
                    buildUrl(endpoint),
                    HttpMethod.POST,
                    entity,
                    responseType);
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    public <T, R> APIResponse<T> put(String endpoint, R request, ParameterizedTypeReference<T> responseType) {
        try {
            HttpEntity<R> entity = new HttpEntity<>(request, createHeaders());
            ResponseEntity<T> response = restTemplate.exchange(buildUrl(endpoint), HttpMethod.PUT,
                    entity, responseType);
            return new APIResponse<>(response.getStatusCode().value(), "Success", response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    // TODO this needs updating because we may need to parse data to see what we
    // want to delete
    public APIResponse<Void> delete(String endpoint) {
        try {
            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
            restTemplate.exchange(buildUrl(endpoint), HttpMethod.DELETE, entity, Void.class);
            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Deleted successfully");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    public <R> APIResponse<Void> delete(String endpoint, R request) {
        try {
            // Create an HttpEntity with the request body and headers
            HttpEntity<R> entity = new HttpEntity<>(request, createHeaders());

            // Perform the DELETE request
            restTemplate.exchange(buildUrl(endpoint), HttpMethod.DELETE, entity, Void.class);

            // Return a success response
            return new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Deleted successfully");
        } catch (HttpClientErrorException e) {
            // Handle client errors (e.g., 404 Not Found)
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return new APIResponse<>(HttpStatus.NOT_FOUND.value(), "Resource not found", null);
            }
            return new APIResponse<>(e.getStatusCode().value(), "Client Error: " + e.getMessage(), null);
        } catch (Exception e) {
            // Handle unexpected errors
            return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred: " + e.getMessage(), null);
        }
    }
}
