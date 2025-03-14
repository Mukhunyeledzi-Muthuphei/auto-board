package com.example.auto_board_shell.helpers;

public class CurrentUser {

    private static String token;
    private static String userName;

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
}
