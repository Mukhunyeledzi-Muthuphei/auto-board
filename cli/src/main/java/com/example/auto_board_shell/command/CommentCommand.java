package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CommentCommand {

    @Autowired
    private RequestService requestService;

    // comments-view --taskId 2
    @ShellMethod(key = "comments-view", value = "View comments for a task")
    public void commentsView(
            @ShellOption(value = "--taskId", help = "Task ID") String task_id
    ) {
        // TODO View comments for a task
        // TODO  1. user must be project member
        System.out.println("test");
    }

    // comment-create --taskId 2 --text "Please complete by friday"
    @ShellMethod(key = "comment-create", value = "Create a comment for a task")
    public void commentCreate(
            @ShellOption(value = "--taskId", help = "Task ID") String task_id,
            @ShellOption(value = "--text", help = "Text") String text
    ) {
        // TODO Create a comment for a task
        // TODO  1. user making request is the user_id ref
        // TODO  2. user must have access to task (validations on needed on the API)
        System.out.println("test");
    }

    // comment-update --taskId 2 --commentId 2 --text "Please complete by thursday"
    @ShellMethod(key = "comment-update", value = "Update a comment for a task")
    public void updateComment(
            @ShellOption(value = "--taskId", help = "Task ID") String task_id,
            @ShellOption(value = "--commentId", help = "Comment ID") String comment_id,
            @ShellOption(value = "--text", help = "Text") String text
    ) {
        // TODO Update comment for a task
        // TODO  1. user must have access to task (validations on needed on the API)
        System.out.println("test");
    }

    // comment-delete --taskId 2 --commentId 2
    @ShellMethod(key = "comment-delete", value = "Delete a comment for a task")
    public void deleteComment(
            @ShellOption(value = "--taskId", help = "Task ID") String task_id,
            @ShellOption(value = "--commentId", help = "Comment ID") String comment_id
    ) {
        // TODO Delete comment for a task
        // TODO  1. Only project members can delete comments
        System.out.println("test");
    }
}
