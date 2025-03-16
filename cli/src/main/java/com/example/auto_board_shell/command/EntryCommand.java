package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.ShellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EntryCommand {

    @Autowired
    private ShellService shellService;

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

}