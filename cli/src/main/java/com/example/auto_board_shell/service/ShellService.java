package com.example.auto_board_shell.service;

import org.springframework.stereotype.Service;

@Service
public class ShellService {

    public void printSuccess(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    public void printError(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    public void printInfo(String message) {
        System.out.println("\u001B[36m" + message + "\u001B[0m");
    }

    public void printWarning(String message) {
        System.out.println("\u001B[33m" + message + "\u001B[0m");
    }

    public void printHeading(String heading) {
        System.out.println("\u001B[1;34m" + heading + "\u001B[0m");
    }

    // For tables and structured output
    public void printTable(String[] headers, String[][] data) {
        // Calculate column widths
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                colWidths[i] = Math.max(colWidths[i], row[i].length());
            }
        }

        // Print headers
        StringBuilder headerLine = new StringBuilder();
        StringBuilder dividerLine = new StringBuilder();

        for (int i = 0; i < headers.length; i++) {
            headerLine.append(String.format("| %-" + colWidths[i] + "s ", headers[i]));
            dividerLine.append("+");
            for (int j = 0; j < colWidths[i] + 2; j++) {
                dividerLine.append("-");
            }
        }
        headerLine.append("|");
        dividerLine.append("+");

        System.out.println(dividerLine.toString());
        System.out.println(headerLine.toString());
        System.out.println(dividerLine.toString());

        // Print data
        for (String[] row : data) {
            StringBuilder dataLine = new StringBuilder();
            for (int i = 0; i < row.length; i++) {
                dataLine.append(String.format("| %-" + colWidths[i] + "s ", row[i]));
            }
            dataLine.append("|");
            System.out.println(dataLine.toString());
        }

        System.out.println(dividerLine.toString());
    }
}
