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

@ShellComponent
public class GoogleLoginShell {

    private final HttpClient client = HttpClient.newHttpClient();
    private String idToken;

    @ShellMethod(key = "login", value = "Login to Google and fetch user info")
    public void login() throws IOException, InterruptedException {
        // Start local callback server
        CompletableFuture<String> tokenFuture = startLocalCallbackServer();

        // 1️⃣ Request login URL from API
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/auth/login-url"))
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
                decodeAndStoreUserInfo(idToken);
            } else {
                System.err.println("Failed to retrieve id token.");
            }
        } catch (Exception e) {
            System.err.println("Error during token retrieval: " + e.getMessage());
        }
    }

    private CompletableFuture<String> startLocalCallbackServer() throws IOException {
        CompletableFuture<String> tokenFuture = new CompletableFuture<>();
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/auth/callback", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response;
                if ("GET".equals(exchange.getRequestMethod())) {
                    String query = exchange.getRequestURI().getQuery();
                    System.out.println("Query: " + query);
                    String code = query != null && query.startsWith("code=") ? query.substring(5) : null;
                    if (code != null) {
                        try {
                            // Send the code to the /auth/callback API to get the id token
                            HttpRequest tokenRequest = HttpRequest.newBuilder()
                                    .uri(URI.create("http://localhost:8080/auth/callback?code=" + code))
                                    .GET()
                                    .build();

                            HttpResponse<String> tokenResponse = client.send(tokenRequest,
                                    HttpResponse.BodyHandlers.ofString());
                            String token = tokenResponse.body();

                            if (token != null && !token.isEmpty()) {
                                tokenFuture.complete(token);
                                response = "id token received. You can close this window.";
                                exchange.sendResponseHeaders(200, response.getBytes().length);
                            } else {
                                response = "Failed to retrieve id token.";
                                exchange.sendResponseHeaders(400, response.getBytes().length);
                            }
                        } catch (Exception e) {
                            response = "Error during token retrieval: " + e.getMessage();
                            exchange.sendResponseHeaders(500, response.getBytes().length);
                        }
                    } else {
                        response = "Failed to retrieve authorization code.";
                        exchange.sendResponseHeaders(400, response.getBytes().length);
                    }
                } else {
                    response = "Method Not Allowed.";
                    exchange.sendResponseHeaders(405, response.getBytes().length);
                }
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                exchange.close();
                server.stop(0);
            }
        });
        server.start();
        return tokenFuture;
    }

    private void decodeAndStoreUserInfo(String idToken) {
        try {
            CurrentUser.setToken(idToken);

            DecodedJWT jwt = JWT.decode(idToken);
            String userInfo = new String(java.util.Base64.getDecoder().decode(jwt.getPayload()));

            // Parse user info to extract first name
            JSONObject userInfoJson = new JSONObject(userInfo);
            String firstName = userInfoJson.optString("given_name", "Unknown");

            // Set current user with first name
            CurrentUser.setUserName(firstName);

            // Store id token and user info in a file
            try (FileWriter fileWriter = new FileWriter("user_info.txt")) {
                fileWriter.write("ID Token: " + idToken + "\n");
                fileWriter.write("User Info: " + userInfo);
            }
        } catch (Exception e) {
            System.err.println("Failed to decode id token: " + e.getMessage());
        }
    }
}
