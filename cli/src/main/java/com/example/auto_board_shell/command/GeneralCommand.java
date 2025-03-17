package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.FormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GeneralCommand {

    @Autowired
    private FormatterService formatterService;

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
            formatterService.printError("Error clearing console!");
        }
    }

    public void displayBanner() {
        String bannerText = """
                     \n
                     █████╗ ██╗   ██╗████████╗ ██████╗ ██████╗  ██████╗  █████╗ ██████╗ ██████╗
                    ██╔══██╗██║   ██║╚══██╔══╝██╔═══██╗██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔══██╗
                    ███████║██║   ██║   ██║   ██║   ██║██████╔╝██║   ██║███████║██████╔╝██║  ██║
                    ██╔══██║██║   ██║   ██║   ██║   ██║██╔══██╗██║   ██║██╔══██║██╔══██╗██║  ██║
                    ██║  ██║╚██████╔╝   ██║   ╚██████╔╝██████╔╝╚██████╔╝██║  ██║██║  ██║██████╔╝
                    ╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝
                    \n
              """;
        formatterService.printTheme(bannerText);
    }

    public void displayWelcome() {
        formatterService.printTheme("\nWelcome!");
    }

    public void displayLogin() {
        formatterService.printInfo("Type 'login' to sign in with your Google account.");
    }

    public void displayHelp() {
        formatterService.printInfo("Type 'help' to view available commands.");
    }

    public void displayGoodbye() {
        formatterService.printInfo("\nGoodbye! Thanks for using Autoboard.");
    }
}