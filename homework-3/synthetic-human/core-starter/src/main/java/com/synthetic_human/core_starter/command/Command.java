package com.synthetic_human.core_starter.command;

import com.synthetic_human.core_starter.error.CommandValidationException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record Command(
        String description,
        CommandPriority priority,
        String author,
        String time
) {
    public void validate() {
        if (description.length() > 1000)
            throw new CommandValidationException("The command description must not exceed 1000 characters");
        if (author.length() > 100)
            throw new CommandValidationException("Author details should not exceed 100 characters");
        try {
            LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new CommandValidationException("The string must comply with ISO-8601 format");
        }
    }

}
