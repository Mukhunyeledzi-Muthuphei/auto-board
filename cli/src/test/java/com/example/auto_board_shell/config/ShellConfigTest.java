package com.example.auto_board_shell.config;

import com.example.auto_board_shell.helpers.CurrentUser;
import org.jline.utils.AttributedString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.shell.jline.PromptProvider;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class ShellConfigTest {

    private ShellConfig shellConfig;
    private PromptProvider promptProvider;

    @BeforeEach
    void setUp() {
        shellConfig = new ShellConfig();
        promptProvider = shellConfig.promptProvider();
    }

    @Test
    void testPromptWithoutUser() {
        CurrentUser.setUserName(null); // Simulate no logged-in user
        String expectedPrefix = "autoboard-cli | ";
        AttributedString prompt = promptProvider.getPrompt();

        assertTrue(prompt.toAnsi().startsWith(expectedPrefix));
        assertTrue(prompt.toAnsi().contains(">")); // Ensure the prompt ends correctly
    }

    @Test
    void testPromptWithUser() {
        CurrentUser.setUserName("testUser");
        String expectedPrefix = "autoboard-cli@testUser | ";
        AttributedString prompt = promptProvider.getPrompt();

        assertTrue(prompt.toAnsi().startsWith(expectedPrefix));
        assertTrue(prompt.toAnsi().contains(">")); // Ensure the prompt ends correctly
    }

    @Test
    void testPromptContainsCurrentTime() {
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        AttributedString prompt = promptProvider.getPrompt();

        assertTrue(prompt.toAnsi().contains(currentTime));
    }
}
