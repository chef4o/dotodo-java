package com.pago.dotodo.common.scheduleJobs;

import com.pago.dotodo.common.service.EmailService;
import com.pago.dotodo.note.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskReminderScheduler {
    private final EmailService emailService;
    private final NoteService noteService;
    private final Logger LOGGER = LoggerFactory.getLogger(TaskReminderScheduler.class);

    public TaskReminderScheduler(EmailService emailService, NoteService noteService) {
        this.emailService = emailService;
        this.noteService = noteService;
    }

    @Scheduled(cron = "0 1 1 * * *")
//    @Scheduled(cron = "0 * * * * *") //cron for every minute
    public void sendTaskReminderEmails() {
        LOGGER.info("Sending task reminder emails initiated");
        noteService.getExpiringNotesEmails()
                .forEach(email -> emailService.sendExpiringTodoEmail(email, "notes"));
        LOGGER.info("Sending task reminder emails completed");
    }
}
