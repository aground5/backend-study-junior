package com.gdgku.study.backend.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ProdNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(ProdNotificationService.class);

    @Override
    public String sendNotification(String message) {
        log.info("[PROD RELEASE] Production Notification dispatched: {}", message);
        return "[PROD] Production Notification Dispatched: " + message;
    }
}
