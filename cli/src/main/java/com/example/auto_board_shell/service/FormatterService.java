package com.example.auto_board_shell.service;

import org.springframework.stereotype.Service;
import org.fusesource.jansi.Ansi;

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

    public void printSuccess(String text) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a(text).reset());
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
        // Compute column widths by mapping each header string to its length.
        // This converts the `headers` list into a stream, applies `String::length` to get each header’s length,
        // and collects the results into a list of integers
        List<Integer> colWidths = headers.stream()
                .map(String::length)
                .collect(Collectors.toList());

        // Adjust column widths based on the longest value in each column.
        // Iterates over each row in `data`, and for each column, updates `colWidths`
        // to store the maximum length found so far.
        for (List<String> row : data) {
            for (int i = 0; i < row.size(); i++) {
                colWidths.set(i, Math.max(colWidths.get(i), row.get(i).length()));
            }
        }

        // Create a divider line for a table based on column widths.
        // Each column width is used to generate a segment like "+---+" where the dashes match the column width.
        // The segments are joined together to form a full horizontal divider line.
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

    // Prints a formatted row with left-aligned columns, each padded to its respective width.
    // Iterates over the row, formats each cell, and joins them into a single string.
    // Prints the resulting row to the console.
    private void printRow(List<String> row, List<Integer> colWidths) {
        String rowLine = IntStream.range(0, row.size())
                .mapToObj(i -> String.format("| %-" + colWidths.get(i) + "s ", row.get(i)))
                .collect(Collectors.joining()) + "|";
        System.out.println(rowLine);
    }

    // Displays a prompt message and returns the user's input as a string.
    public String prompt(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

}
