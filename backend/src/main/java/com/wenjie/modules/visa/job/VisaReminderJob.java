package com.wenjie.modules.visa.job;

import com.wenjie.modules.visa.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VisaReminderJob {

    private final VisaService visaService;

    @Scheduled(cron = "${app.visa.reminder-cron:0 10 2 * * ?}")
    public void generateVisaAlerts() {
        visaService.generateAlerts();
    }
}
