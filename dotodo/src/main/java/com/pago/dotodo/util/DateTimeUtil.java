package com.pago.dotodo.util;

import com.pago.dotodo.model.dto.NoteDto;
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

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        if (!date.isBlank()) {
            try {
                LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

                if (time == null || time.isEmpty()) {
                    LocalDate today = LocalDate.now();
                    return parsedDate.isAfter(today);
                } else {
                    LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
                    LocalDateTime dateTime = LocalDateTime.of(parsedDate, parsedTime);

                    LocalDateTime now = LocalDateTime.now();
                    return dateTime.isAfter(now);
                }
            } catch (DateTimeParseException e) {
                return false;
            }
        }

        return false;
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
            } else if (hoursUntilDue >= 0) {
                note.setDueHours((int) hoursUntilDue);
            } else {
                note.setDueHours(-1);
            }
        }

        return note;
    }

    public String formatToISODate(String date, String currentFormat) {
        if (date.isBlank()) {
            return "";
        }
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(currentFormat);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, inputFormatter).format(outputFormatter);
    }

    /**
     * Converts a date string in the format "dd/MM/yyyy" to LocalDate.
     *
     * @param dateString The date string to convert.
     * @return The corresponding LocalDate object, or null if parsing fails.
     */
    public LocalDate formatStringToDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateString, dateFormatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse date: " + dateString, e);
        }
    }

    public String formatDateToString(LocalDateTime date) {
        return date != null ? date.toLocalDate().format(dateFormatter) : "";
    }

    public String formatTimeToString(LocalDateTime date) {
        return date.toLocalTime().toString();
    }

    public boolean datesDiffer(String date1, String date2) {
        if (date1 == null && date2 == null) return false;
        if (date1 == null || date2 == null) return true;
        return !date1.equals(date2);
    }
}
