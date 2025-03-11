package com.example.autoboard;

// import org.springframework.beans.factory.annotation.Lazy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.Map;

@ShellComponent
public class GoogleAuthCommands {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public GoogleAuthCommands(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @ShellMethod(key = "login", value = "Log in using Google")
    public String login() {
        return "Please visit: http://localhost:8080/oauth2/authorization/google to log in.";
    }

    @ShellMethod(key = "userinfo", value = "Print user information")
    public String userInfo(@AuthenticationPrincipal OAuth2AuthenticationToken authToken) {
        if (authToken == null) {
            return "You are not logged in.";
        }

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(), authToken.getName());

        String accessToken = client.getAccessToken().getTokenValue();
        RestTemplate restTemplate = new RestTemplate();
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";

        Map<String, Object> response = restTemplate.getForObject(
                userInfoEndpoint + "?access_token=" + accessToken, Map.class);

        return response.toString();
    }
}