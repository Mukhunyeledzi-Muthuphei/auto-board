package com.example.auto_board_shell.command;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.awt.Desktop;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.auto_board_shell.helpers.CurrentUser;
import org.json.JSONObject;
import com.example.auto_board_shell.service.GoogleAuthService;
import org.springframework.beans.factory.annotation.Value;

@ShellComponent
public class GoogleAuthCommand {

    private final HttpClient client = HttpClient.newHttpClient();
    private String idToken;

    @Value("${cli.api.base-url}")
    private String baseUrl;

    @ShellMethod(key = "login", value = "Login to Google and fetch user info")
    public void login() throws IOException, InterruptedException {
        // Start local callback server
        CompletableFuture<String> tokenFuture = GoogleAuthService.startLocalCallbackServer(client, baseUrl);

        // 1️⃣ Request login URL from API
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/auth/login-url"))
                .GET()
                .build();

        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        String loginUrl = loginResponse.body();

        System.out.println("Open this URL to log in: " + loginUrl);

        // Open browser automatically using cmd
        try {
            new ProcessBuilder("cmd", "/c", "start", loginUrl).start();
        } catch (IOException e) {
            System.err.println("Failed to open browser: " + e.getMessage());
        }

        try {
            // 2️⃣ Wait for user to authenticate and retrieve id token
            idToken = tokenFuture.get(60, TimeUnit.SECONDS);

            if (idToken != null) {
                GoogleAuthService.decodeAndStoreUserInfo(idToken);
            } else {
                System.err.println("Failed to retrieve id token.");
            }
        } catch (Exception e) {
            System.err.println("Error during token retrieval: " + e.getMessage());
        }
    }

    @ShellMethod(key = "logout", value = "Logout from Google")
    public void logout() {
        CurrentUser.clear();
        GoogleAuthService.clearUserInfo();
        System.out.println("Logged out successfully.");
    }

    @ShellMethod(key = "delete", value = "Delete user info")
    public void delete() {
        GoogleAuthService.deleteUserInfo(baseUrl);
    }

}
