package com.example.auto_board_shell;

import com.example.auto_board_shell.command.EntryCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AutoBoardShellApplication {

	@Autowired
	private EntryCommand entryCommand;

	public static void main(String[] args) {
		SpringApplication.run(AutoBoardShellApplication.class, args);
	}

	@EventListener(ApplicationStartedEvent.class)
	public void onApplicationStarted() {
		entryCommand.clearTerminal();
		entryCommand.displayBanner();
		entryCommand.displayWelcome();
		entryCommand.displayHelp();
	}

}