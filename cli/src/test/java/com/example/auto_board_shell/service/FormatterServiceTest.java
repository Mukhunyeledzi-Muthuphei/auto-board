package com.example.auto_board_shell.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class FormatterServiceTest {

    private FormatterService formatterService;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        // Set up the mock Scanner and FormatterService instance
        mockScanner = mock(Scanner.class);
        formatterService = new FormatterService() {
            @Override
            public String prompt(String message) {
                return mockScanner.nextLine();
            }
        };
        System.setOut(new PrintStream(outputStreamCaptor));  // Capturing System.out.println() output
    }

    @Test
    void testPrintTheme() {
        // Arrange
        String text = "Theme Text";

        // Act
        formatterService.printTheme(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }

    @Test
    void testPrintThemeBold() {
        // Arrange
        String text = "Bold Theme Text";

        // Act
        formatterService.printThemeBold(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }

    @Test
    void testPrintInfo() {
        // Arrange
        String text = "Info Text";

        // Act
        formatterService.printInfo(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }

    @Test
    void testPrintError() {
        // Arrange
        String text = "Error Text";

        // Act
        formatterService.printError(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }

    @Test
    void testPrintWarning() {
        // Arrange
        String text = "Warning Text";

        // Act
        formatterService.printWarning(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }

    @Test
    void testPrintTable() {
        // Arrange
        List<String> headers = Arrays.asList("Header1", "Header2");
        List<List<String>> data = Arrays.asList(
                Arrays.asList("Row1-1", "Row1-2"),
                Arrays.asList("Row2-1", "Row2-2")
        );

        // Act
        formatterService.printTable(headers, data);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains("+---------+---------+\r\n| Header1 | Header2 |\r\n+---------+---------+\r\n| Row1-1  | Row1-2  |\r\n| Row2-1  | Row2-2  |\r\n+---------+---------+"));

    }

    @Test
    void testPrompt() {
        // Arrange
        String promptMessage = "Enter something: ";
        String inputMessage = "User Input";

        when(mockScanner.nextLine()).thenReturn(inputMessage);

        // Act
        String response = formatterService.prompt(promptMessage);

        // Assert
        assertEquals(inputMessage, response);
    }

    @Test
    void testPrintSuccess() {
        // Arrange
        String text = "Success Message";

        // Act
        formatterService.printSuccess(text);

        // Assert
        assertTrue(outputStreamCaptor.toString().contains(text));
    }
}
