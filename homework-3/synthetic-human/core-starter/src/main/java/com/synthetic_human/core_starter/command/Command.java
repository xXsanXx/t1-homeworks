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
            throw new CommandValidationException("Описание команды не должно превышать 1000 символов");
        if (author.length() > 100)
            throw new CommandValidationException("Данные об авторе не должны превышать 100 символов");
        try {
            LocalDate.parse(time, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new CommandValidationException("Строка должна соответствовать формату ISO-8601 ");
        }
    }

}
