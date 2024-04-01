package com.example.tasknewspring.validators;

import com.example.tasknewspring.dto.TaskDto;
import com.example.tasknewspring.util.LocaleMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TaskValidator {
    public static List<String> validateTask(TaskDto taskDto, Locale locale, LocaleMessage localeMessage) {

        List<String> errors = new ArrayList<>();
        if (taskDto == null) {
            errors.add(localeMessage.getLocaleMessage("validation.task.null", locale));
            return errors;
        }
        if (taskDto.getDescription() == null || taskDto.getDescription().isBlank()) {
            errors.add(localeMessage.getLocaleMessage("validation.task.description", locale));
        }
        if (taskDto.getDueDate() == null || taskDto.getDueDate().isBefore(LocalDate.now())) {
            errors.add(localeMessage.getLocaleMessage("validation.task.dueDate", locale));
        }
        if (taskDto.getStatus() == null) {
            errors.add(localeMessage.getLocaleMessage("validation.task.state", locale));
        }
        if (taskDto.getUserId() == null) {
            errors.add(localeMessage.getLocaleMessage("validation.task.userId", locale));
        }
        return errors;
    }
}
