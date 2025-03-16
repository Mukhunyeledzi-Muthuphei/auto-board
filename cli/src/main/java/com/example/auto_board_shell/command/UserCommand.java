package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class UserCommand {

    @Autowired
    private APIService apiService;

    // users-view
    @ShellMethod(key = "users-view", value = "View all users")
    public void usersView() {
        System.out.println("test");
    }

    // users-search --pattern "Chris"
    @ShellMethod(key = "users-search", value = "Search users on first or last name")
    public void usersSearch(
            @ShellOption(value = "--pattern", help = "Phrase to pattern match") String pattern
    ) {
        System.out.println("Searching for users whose details contain the keyword: " + pattern);
    }

}
