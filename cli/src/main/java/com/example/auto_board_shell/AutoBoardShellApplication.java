package com.example.auto_board_shell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoBoardShellApplication {

	public static void main(String[] args) {

		String welcomeToKillerBeans = "\n" + "\u001B[34m" +
				"		_              _                         _\n" +
				"     /\\        | |            | |                       | |\n" +
				"    /  \\  _   _| |_ ___ ______| |__   ___   __ _ _ __ __| |\n" +
				"   / /\\ \\| | | | __/ _ \\______| '_ \\ / _ \\ / _` | '__/ _` |\n" +
				"  / ____ \\ |_| | || (_) |     | |_) | (_) | (_| | | | (_| |\n" +
				" /_/    \\_\\__,_|\\__\\___/      |_.__/ \\___/ \\__,_|_|  \\__,_|\n" +
				"                                                                                         \u001B[0m";
		System.out.println(welcomeToKillerBeans + "\n");

		System.out.println(
				"------------------------------------------------" +
						"\u001B[38;5;208m" + "\nWelcome to Auto-board  \n" + "\u001B[0m" +

						"------------------------------------------------" +
						// "\nType \u001B[32m help\u001B[0m to see available commands." +
						"\nType \u001B[34m login \u001B[0m to get access to all commands\n" +
						"------------------------------------------------\n");

		SpringApplication.run(AutoBoardShellApplication.class, args);
	}

}
