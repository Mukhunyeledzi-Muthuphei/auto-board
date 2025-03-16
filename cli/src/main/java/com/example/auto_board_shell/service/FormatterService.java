package com.example.auto_board_shell.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FormatterService {

    public void printTable(List<String> headers, List<List<String>> data) {
        // Determine column widths
        List<Integer> colWidths = headers.stream()
                .map(header -> header.length())
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
                .mapToObj(i -> String.format("| %-"+ colWidths.get(i) +"s ", row.get(i)))
                .collect(Collectors.joining()) + "|";
        System.out.println(rowLine);
    }

}
