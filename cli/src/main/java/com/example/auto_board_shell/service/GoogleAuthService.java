package com.example.auto_board_shell.service;

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
import java.util.Currency;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.auto_board_shell.helpers.CurrentUser;
import org.json.JSONObject;
import com.example.auto_board_shell.service.GoogleAuthService;

public class GoogleAuthService {
    public static CompletableFuture<String> startLocalCallbackServer(HttpClient client) throws IOException {
        CompletableFuture<String> tokenFuture = new CompletableFuture<>();
        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
        server.createContext("/auth/callback", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String response;
                if ("GET".equals(exchange.getRequestMethod())) {
                    String query = exchange.getRequestURI().getQuery();
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

    public static void decodeAndStoreUserInfo(String idToken) {
        try {
            CurrentUser.setToken(idToken);

            DecodedJWT jwt = JWT.decode(idToken);
            String userInfo = new String(java.util.Base64.getDecoder().decode(jwt.getPayload()));

            // Parse user info to extract first name
            JSONObject userInfoJson = new JSONObject(userInfo);
            String firstName = userInfoJson.optString("given_name", "Unknown");

            // Set current user with first name
            CurrentUser.setUserName(firstName);

            String userId = userInfoJson.optString("sub", null);
            CurrentUser.setId(userId);

            // Store id token and user info in a file
            try (FileWriter fileWriter = new FileWriter("user_info.txt")) {
                fileWriter.write("ID Token: " + idToken + "\n");
                fileWriter.write("User Info: " + userInfo);
            }
        } catch (Exception e) {
            System.err.println("Failed to decode id token: " + e.getMessage());
        }
    }

    public static void clearUserInfo() {
        try {
            CurrentUser.setToken(null);
            CurrentUser.setUserName(null);
            CurrentUser.setId(null);
            new FileWriter("user_info.txt").close();
        } catch (IOException e) {
            System.err.println("Failed to delete user info: " + e.getMessage());
        }
    }

    public static void deleteUserInfo() {
        try {
            CurrentUser.setUserName(null);

            new FileWriter("user_info.txt").close();

            // Call delete API to delete user info from the database
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users/" + CurrentUser.getId()))
                    .header("Authorization", CurrentUser.getToken())
                    .DELETE()
                    .build();
            CurrentUser.setId(null);
            CurrentUser.setToken(null);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                System.out.println("User info deleted successfully from the database.");
            } else {
                System.err.println("Failed to delete user info from the database: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to delete user info: " + e.getMessage());
        }
    }

}
