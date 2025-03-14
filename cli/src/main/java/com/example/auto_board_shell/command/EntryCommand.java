package com.example.auto_board_shell.command;

import com.example.auto_board_shell.config.UserSession;
import com.example.auto_board_shell.service.ShellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntryCommand {

    @Autowired
    private ShellService shellService;

    @Autowired
    private UserSession userSession;

    public void clearTerminal() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error clearing console!");
        }
    }

    public void displayBanner() {
        String banner = """
                     \n
                     █████╗ ██╗   ██╗████████╗ ██████╗ ██████╗  ██████╗  █████╗ ██████╗ ██████╗
                    ██╔══██╗██║   ██║╚══██╔══╝██╔═══██╗██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔══██╗
                    ███████║██║   ██║   ██║   ██║   ██║██████╔╝██║   ██║███████║██████╔╝██║  ██║
                    ██╔══██║██║   ██║   ██║   ██║   ██║██╔══██╗██║   ██║██╔══██║██╔══██╗██║  ██║
                    ██║  ██║╚██████╔╝   ██║   ╚██████╔╝██████╔╝╚██████╔╝██║  ██║██║  ██║██████╔╝
                    ╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝
                    \n
              """;
        shellService.printInfo(banner);
    }

    public void displayWelcome() {
        shellService.printHeading("\nWelcome to Autoboard!");
    }

    public void displayHelp() {
        shellService.printInfo("Type 'help' to view available commands.");
    }

    public void displayUserLoginStatus() {
        try {




            if (userSession.isAuthenticated()) {
                shellService.printSuccess("You are logged in as: " + userSession.getUserName() +
                        " (" + userSession.getUserEmail() + ")");
                shellService.printInfo("Type 'task-list' to see your tasks or 'help' to see all available commands.");
            } else {
                shellService.printWarning("You are not logged in. Please use 'login' to authenticate.");
                shellService.printInfo("Type 'help' to see all available commands.");
            }
        } catch (Exception e) {
            // If banner can't be loaded, just show a simple message
            shellService.printHeading("\nWelcome to Autoboard!");

            if (userSession.isAuthenticated()) {
                shellService.printSuccess("You are logged in as: " + userSession.getUserName());
                shellService.printInfo("Type 'task-list' to see your tasks or 'help' to see all available commands.");
            } else {
                shellService.printWarning("You are not logged in. Please use 'login' to authenticate.");
                shellService.printInfo("Type 'help' to see all available commands.");
            }
        }
    }

}