package com.pago.dotodo.common.scheduleJobs;

import com.pago.dotodo.common.service.EmailService;
import com.pago.dotodo.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BirthdayScheduler {
    private final Logger LOGGER = LoggerFactory.getLogger(BirthdayScheduler.class);
    private final EmailService emailService;
    private final UserService userService;

    public BirthdayScheduler(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @Scheduled(cron = "1 0 * * * *")
    public void sendBirthdayEmails() {
        LOGGER.info("Sending Birthday wishes emails initiated");

        userService.getUsersWithBirthday().forEach(emailService::sendBirthdayEmail);

        LOGGER.info("Sending Birthday wishes emails completed");
    }
}
