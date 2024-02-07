package org.algorhythm.bankingmanagmentsystem;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyTask {

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at 12:00 AM
    public void runDailyTask() {
        // Your task logic here
        System.out.println("Daily task running...");
    }
}
