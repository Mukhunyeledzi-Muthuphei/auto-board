package com.example.auto_board_shell.command;

import com.example.auto_board_shell.service.FormatterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneralCommandTest {

    @Mock
    private FormatterService formatterService;

    @InjectMocks
    private GeneralCommand generalCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDisplayBanner() {
        generalCommand.displayBanner();
        verify(formatterService).printTheme(anyString());
    }

    @Test
    void testDisplayWelcome() {
        generalCommand.displayWelcome();
        verify(formatterService).printTheme("\nWelcome!");
    }

    @Test
    void testDisplayLogin() {
        generalCommand.displayLogin();
        verify(formatterService).printInfo("Type 'login' to sign in with your Google account.");
    }

    @Test
    void testDisplayHelp() {
        generalCommand.displayHelp();
        verify(formatterService).printInfo("Type 'help' to view available commands.");
    }

    @Test
    void testDisplayGoodbye() {
        generalCommand.displayGoodbye();
        verify(formatterService).printInfo("\nGoodbye! Thanks for using Autoboard.");
    }

    @Test
    void testClearTerminal() {
        generalCommand.clearTerminal();
        verify(formatterService, never()).printError("Error clearing console!");
    }
}
