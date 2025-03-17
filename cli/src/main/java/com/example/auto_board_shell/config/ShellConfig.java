package com.example.auto_board_shell.config;

import org.jline.utils.AttributedString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.example.auto_board_shell.helpers.CurrentUser;
import java.util.Objects;

@Configuration
public class ShellConfig {

    @Bean
    public PromptProvider promptProvider() {
        return () -> {
            String projectName = "autoboard-cli";
            String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            return AttributedString
                    .fromAnsi(projectName
                            + (Objects.nonNull(CurrentUser.getUserName()) ? "@" + CurrentUser.getUserName() : "")
                            + " | " + currentTime + " > ");
        };
    }
}