package com.example.auto_board_shell.helpers;

public class CurrentUser {

    private static String token;
    private static String userName;
    private static String id;

    public static void clear() {
        token = null;
        userName = null;
        id = null;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CurrentUser.token = token;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        CurrentUser.id = id;
    }
}
