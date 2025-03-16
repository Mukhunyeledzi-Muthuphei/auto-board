package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.APIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ActivityLogCommand {

    @Autowired
    private APIService apiService;

    // activity-log-project --id 2
    @ShellMethod(key = "activity-log-project", value = "View all task logs for a project")
    public void activityLogProject(
            @ShellOption(value = "--id", help = "Project id") String id
    ) {
        // TODO print out logs for all tasks associated to a project (user must have access)
        System.out.println("test");
        // Object[] logs = apiService.get("/activity-log/project/" + id, Object[].class);
    }

    // activity-log-task --id 2
    @ShellMethod(key = "activity-log-task", value = "View task logs")
    public void activityLogTask(
            @ShellOption(value = "--id", help = "Task id") String id
    ) {
        // TODO print out logs for a task (user must have access)
        System.out.println("test");
        // Object[] logs = apiService.get("/activity-log/task/" + id, Object[].class);
    }
}
