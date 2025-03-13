package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import com.example.auto_board_shell.service.ShellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ShellComponent
public class ActivityLogCommand {

    @Autowired
    private APIService apiService;

    @Autowired
    private ShellService shellService;

    @ShellMethod(key = "activity-log", value = "Test command")
    public void simpleTest() {
        System.out.println("Simple test command executed");
    }

    @ShellMethod(key = "act-logs", value = "Act logs")
    public void actLog() {
        System.out.println("Fetching Logs...");
    }

    @ShellMethod(key = "simple-test", value = "List all activity logs")
    public void listLogs() {
        try {
            System.out.println("Fetching Logs...");

            // Fetching logs
            Object[] logs = apiService.get("/activity-log", Object[].class);

            if (logs == null || logs.length == 0) {
                System.out.println("No logs found");
            } else {
                List<String[]> tableData = new ArrayList<>();

                for (Object logObj : logs) {
                    if (logObj instanceof Map) {
                        Map<String, Object> log = (Map<String, Object>) logObj;

                        String[] row = new String[4];
                        row[0] = String.valueOf(log.get("id"));

                        // Extract task title from nested "task" object safely
                        Map<String, Object> task = (Map<String, Object>) log.get("task");
                        row[1] = (task != null && task.get("title") != null) ? String.valueOf(task.get("title")) : "No title"; // Default to "No title"

                        row[2] = String.valueOf(log.get("action"));
                        row[3] = String.valueOf(log.get("timestamp"));

                        tableData.add(row);
                    } else {
                        System.out.println("Invalid log format: " + logObj);
                    }
                }

                String[] headers = {"ID", "Task Title", "Action", "Timestamp"};

                shellService.printTable(headers, tableData.toArray(new String[0][]));
            }
        } catch (Exception e) {
            shellService.printError("Error fetching logs: " + e.getMessage());
            e.printStackTrace(); // Additional logging for the stack trace
        }
    }

}
