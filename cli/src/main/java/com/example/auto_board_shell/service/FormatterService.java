package com.example.auto_board_shell.service;

import org.springframework.stereotype.Service;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Scanner;

@Service
public class FormatterService {

    private final Scanner scanner = new Scanner(System.in);

    // Overall project theme - orange
    public void printTheme(String text) {
        System.out.println(Ansi.ansi().fgRgb(255, 165, 0).a(text).reset());
    }

    public void printThemeBold(String text) {
        System.out.println(Ansi.ansi().fgRgb(255, 165, 0).bold().a(text).reset());
    }

    // Info - dark orange
    public void printInfo(String text) {
        System.out.println(Ansi.ansi().fgRgb(204, 114, 0).a(text).reset());
    }

    public void printError(String text) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a(text).reset());
    }

    public void printWarning(String text) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a(text).reset());
    }

    public void printTable(List<String> headers, List<List<String>> data) {
        // Determine column widths
        List<Integer> colWidths = headers.stream()
                .map(String::length)
                .collect(Collectors.toList());

        for (List<String> row : data) {
            for (int i = 0; i < row.size(); i++) {
                colWidths.set(i, Math.max(colWidths.get(i), row.get(i).length()));
            }
        }

        // Create divider line
        String dividerLine = colWidths.stream()
                .map(width -> "+-" + "-".repeat(width) + "-")
                .collect(Collectors.joining()) + "+";

        // Print table
        System.out.println(dividerLine);
        printRow(headers, colWidths);
        System.out.println(dividerLine);
        data.forEach(row -> printRow(row, colWidths));
        System.out.println(dividerLine);
    }

    private void printRow(List<String> row, List<Integer> colWidths) {
        String rowLine = IntStream.range(0, row.size())
                .mapToObj(i -> String.format("| %-" + colWidths.get(i) + "s ", row.get(i)))
                .collect(Collectors.joining()) + "|";
        System.out.println(rowLine);
    }

    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public void printSuccess(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

}
