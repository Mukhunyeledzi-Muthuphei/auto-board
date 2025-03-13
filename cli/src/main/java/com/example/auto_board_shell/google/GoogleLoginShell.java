//package com.example.auto_board_shell.google;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.shell.standard.ShellComponent;
//import org.springframework.shell.standard.ShellMethod;
//import com.sun.net.httpserver.HttpServer;
//
//import java.awt.Desktop;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.net.URI;
//import java.net.URLEncoder;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.nio.charset.StandardCharsets;
//import java.util.Map;
//import java.util.concurrent.CountDownLatch;
//
//@ShellComponent
//public class GoogleLoginShell {
//
//    @Value("${google.client.id}")
//    private String clientId;
//
//    @Value("${google.client.secret}")
//    private String clientSecret;
//
//    @Value("${google.redirect.uri}")
//    private String redirectUri;
//
//    @Value("${google.auth.url}")
//    private String authUrl;
//
//    @Value("${google.token.url}")
//    private String tokenUrl;
//
//    @Value("${google.user.info.url}")
//    private String userInfoUrl;
//
//    private final CountDownLatch latch = new CountDownLatch(1);
//    private String authorizationCode;
//
//    @ShellMethod(key = "login", value = "Login to Google")
//    public void login() throws IOException {
//        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);
//        server.createContext("/callback", exchange -> {
//            String query = exchange.getRequestURI().getQuery();
//            Map<String, String> params = parseQuery(query);
//            authorizationCode = params.get("code");
//            String responseText = "Login successful! You can close this window.";
//            exchange.sendResponseHeaders(200, responseText.length());
//            OutputStream os = exchange.getResponseBody();
//            os.write(responseText.getBytes());
//            os.close();
//            latch.countDown();
//            server.stop(0);
//        });
//        server.start();
//
//        String authUrlWithParams = authUrl + "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
//                "^&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
//                "^&response_type=" + URLEncoder.encode("code", StandardCharsets.UTF_8) +
//                "^&scope=" + URLEncoder.encode("email profile", StandardCharsets.UTF_8) +
//                "^&access_type=" + URLEncoder.encode("offline", StandardCharsets.UTF_8);
//        System.out.println("Opening the following URL in your browser:");
//        System.out.println(authUrlWithParams);
//
//        try {
//            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//                Desktop.getDesktop().browse(URI.create(authUrlWithParams));
//            } else {
//                throw new UnsupportedOperationException("Desktop browsing is not supported.");
//            }
//        } catch (Exception e) {
//            System.err.println(
//                    "Desktop browsing is not supported. Attempting to open the URL using the system's default browser.");
//            try {
//                Runtime.getRuntime().exec(new String[] { "cmd", "/c", "start", authUrlWithParams });
//            } catch (IOException ex) {
//                System.err.println("Failed to open the URL. Please open it manually: " + authUrlWithParams);
//            }
//        }
//
//        try {
//            latch.await();
//            fetchUserInfo(authorizationCode);
//        } catch (Exception e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    private static Map<String, String> parseQuery(String query) {
//        return query == null ? Map.of() : Map.of(query.split("=")[0], query.split("=")[1]);
//    }
//
//    private void fetchUserInfo(String code) throws Exception {
//        HttpClient client = HttpClient.newHttpClient();
//        String requestBody = "client_id=" + clientId + "&client_secret=" + clientSecret +
//                "&redirect_uri=" + redirectUri + "&code=" + code + "&grant_type=authorization_code";
//        HttpRequest tokenRequest = HttpRequest.newBuilder()
//                .uri(URI.create(tokenUrl))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> tokenResponse = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
//        String responseBody = tokenResponse.body();
//
//        if (responseBody.contains("\"access_token\": \"")) {
//            String accessToken = responseBody.split("\"access_token\": \"")[1].split("\"", 2)[0];
//            HttpRequest userRequest = HttpRequest.newBuilder()
//                    .uri(URI.create(userInfoUrl))
//                    .header("Authorization", "Bearer " + accessToken)
//                    .build();
//
//            HttpResponse<String> userResponse = client.send(userRequest, HttpResponse.BodyHandlers.ofString());
//            System.out.println("User Info: " + userResponse.body());
//        } else {
//            System.err.println("Error: Access token not found in the response.");
//            System.err.println("Response: " + responseBody);
//        }
//    }
//}
