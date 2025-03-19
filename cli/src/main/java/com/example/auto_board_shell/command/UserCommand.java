package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIResponse;
import com.example.auto_board_shell.service.FormatterService;
import com.example.auto_board_shell.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
public class UserCommand {

    private final RequestService requestService;
    private final FormatterService formatterService;

    @Autowired
    public UserCommand(RequestService requestService, FormatterService formatterService) {
        this.requestService = requestService;
        this.formatterService = formatterService;
    }

    // users-view
    @ShellMethod(key = "users-list", value = "View users")
    public void usersView() {
        try {
            formatterService.printInfo("Fetching users");

            APIResponse<List<Map<String, Object>>> response = requestService.get("/users", new ParameterizedTypeReference<List<Map<String, Object>>>() {});

            List<Map<String, Object>> users = response.getData();

            List<String> headers = new ArrayList<>(users.get(0).keySet());

            List<List<String>> data = users.stream()
                    .map(status -> headers.stream()
                            .map(key -> String.valueOf(status.getOrDefault(key, "N/A")))
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList());

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching users: " + e.getMessage());
        }
    }

    @ShellMethod(key = "users-search-id", value = "Search users by ID")
    public void usersSearchById(
            @ShellOption(value = "--id", help = "User ID") String id
    ) {
        try {
            formatterService.printInfo("Searching for user with ID: " + id);

            APIResponse<Map<String, Object>> response = requestService.get("/users/" + id, new ParameterizedTypeReference<Map<String, Object>>() {});

            Map<String, Object> user = response.getData();

            if (user == null || user.isEmpty()) {
                formatterService.printWarning("No user found with ID: " + id);
                return;
            }

            List<String> selectedHeaders = List.of("id", "firstName", "lastName");
            List<String> headers = new ArrayList<>(selectedHeaders);

            List<List<String>> data = List.of(headers.stream()
                    .map(key -> String.valueOf(user.getOrDefault(key, "N/A")))
                    .collect(Collectors.toList()));

            formatterService.printTable(headers, data);

        } catch (Exception e) {
            formatterService.printError("Error fetching user: " + e.getMessage());
        }
    }

}
