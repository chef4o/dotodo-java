package com.pago.dotodo.util;

import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.service.InitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateTimeUtilTest {

    private DateTimeUtil dateTimeUtil;

    @MockBean
    private InitService initService;

    @BeforeEach
    void setUp() {
        dateTimeUtil = new DateTimeUtil();
    }

    @Test
    void testFormatToDateTime_withValidDateAndTime() {
        String date = "17/08/2024";
        String time = "15:30";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm");
        LocalDateTime expected = LocalDateTime.of(2024, 8, 17, 15, 30);

        LocalDateTime result = dateTimeUtil.formatToDateTime(date, time, format);

        assertEquals(expected, result);
    }

    @Test
    void testFormatToDateTime_withNullDate() {
        LocalDateTime result = dateTimeUtil.formatToDateTime(null, "15:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertNull(result);
    }

    @Test
    void testIsInFuture_withFutureDate() {
        String date = LocalDate.now().plusDays(1).toString(); // tomorrow
        String time = LocalTime.now().plusHours(1).toString(); // an hour from now

        assertTrue(dateTimeUtil.isInFuture(date, time));
    }

    @Test
    void testIsInFuture_withPastDate() {
        String date = LocalDate.now().minusDays(1).toString(); // yesterday
        String time = LocalTime.now().toString(); // now

        assertFalse(dateTimeUtil.isInFuture(date, time));
    }

    @Test
    void testAddDueDaysHours() {
        NoteDto note1 = new NoteDto();
        note1.setDueDate("17/08/2024");
        note1.setDueTime("15:30");

        NoteDto note2 = new NoteDto();
        note2.setDueDate(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        note2.setDueTime(LocalTime.now().truncatedTo(ChronoUnit.MINUTES).toString());

        List<NoteDto> notes = List.of(note1, note2);
        List<NoteDto> updatedNotes = dateTimeUtil.addDueDaysHours(notes);

        assertNotNull(updatedNotes);
        assertEquals(2, updatedNotes.size());
        assertEquals(1, updatedNotes.get(1).getDueDays());
        assertEquals(-1, updatedNotes.get(0).getDueHours());
    }

    @Test
    void testFormatToISODate() {
        String date = "17/08/2024";
        String result = dateTimeUtil.formatToISODate(date, "dd/MM/yyyy");

        assertEquals("2024-08-17", result);
    }

    @Test
    void testFormatStringToDate_withValidDateString() {
        String dateString = "17/08/2024";
        LocalDate expectedDate = LocalDate.of(2024, 8, 17);

        LocalDate result = dateTimeUtil.formatStringToDate(dateString);

        assertEquals(expectedDate, result);
    }

    @Test
    void testFormatStringToDate_withInvalidDateString() {
        String invalidDateString = "invalid_date";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            dateTimeUtil.formatStringToDate(invalidDateString);
        });

        assertTrue(exception.getMessage().contains("Failed to parse date"));
    }

    @Test
    void testFormatDateToString_withValidDate() {
        LocalDateTime date = LocalDateTime.of(2024, 8, 17, 15, 30);
        String result = dateTimeUtil.formatDateToString(date);

        assertEquals("17/08/2024", result);
    }

    @Test
    void testFormatDateToString_withNullDate() {
        String result = dateTimeUtil.formatDateToString(null);

        assertEquals("", result);
    }

    @Test
    void testFormatTimeToString_withValidTime() {
        LocalDateTime date = LocalDateTime.of(2024, 8, 17, 15, 30);
        String result = dateTimeUtil.formatTimeToString(date);

        assertEquals("15:30", result);
    }

    @Test
    void testDatesDiffer_withDifferentDates() {
        assertTrue(dateTimeUtil.datesDiffer("17/08/2024", "18/08/2024"));
    }

    @Test
    void testDatesDiffer_withSameDates() {
        assertFalse(dateTimeUtil.datesDiffer("17/08/2024", "17/08/2024"));
    }

    @Test
    void testDatesDiffer_withOneNullDate() {
        assertTrue(dateTimeUtil.datesDiffer("17/08/2024", null));
    }
}
