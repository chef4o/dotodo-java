package com.pago.dotodo.util;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.web.mvc.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class DateTimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public LocalDateTime formatToDateTime(String date, String time, DateTimeFormatter format) {
        if (date == null || date.isEmpty()) {
            return null;
        }

        String dateTime = time == null || time.isEmpty()
                ? date + "T00:00:00"
                : date + "T" + time;

        return LocalDateTime.parse(dateTime, format);
    }

    public boolean isInFuture(String date, String time) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

            if (time == null || time.isEmpty()) {
                LocalDate today = LocalDate.now();
                return !parsedDate.isBefore(today);
            } else {
                LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
                LocalDateTime dateTime = LocalDateTime.of(parsedDate, parsedTime);

                LocalDateTime now = LocalDateTime.now();
                return dateTime.isAfter(now);
            }
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    public List<NoteDto> addDueDaysHours(List<NoteDto> notes) {
        return notes.stream()
                .map(this::setDueDaysHours)
                .toList();
    }

    public NoteDto setDueDaysHours(NoteDto note) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime dueDateTime = formatToDateTime(note.getDueDate(), note.getDueTime(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm"));

        if (dueDateTime != null) {
            long daysUntilDue = ChronoUnit.DAYS.between(now.toLocalDate(), dueDateTime.toLocalDate());
            long hoursUntilDue = ChronoUnit.HOURS.between(now, dueDateTime);

            if (daysUntilDue >= 1) {
                note.setDueDays((int) daysUntilDue);
            } else if (hoursUntilDue > 1) {
                note.setDueHours((int) hoursUntilDue);
            }
        }

        return note;
    }
}
