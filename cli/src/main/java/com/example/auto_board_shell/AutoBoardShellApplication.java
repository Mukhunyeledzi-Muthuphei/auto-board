package com.example.auto_board_shell;

import com.example.auto_board_shell.command.GeneralCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AutoBoardShellApplication {

	@Autowired
	private GeneralCommand generalCommand;

	public static void main(String[] args) {
		SpringApplication.run(AutoBoardShellApplication.class, args);
	}

	@EventListener(ApplicationStartedEvent.class)
	public void onApplicationStarted() {
		generalCommand.clearTerminal();
		generalCommand.displayBanner();
		generalCommand.displayWelcome();
		generalCommand.displayLogin();
		generalCommand.displayHelp();
	}

	@EventListener(ContextClosedEvent.class)
	public void onShutdown() {
		generalCommand.displayGoodbye();
	}

}