package com.gdgku.study.backend.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class LocalNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(LocalNotificationService.class);

    @Override
    public String sendNotification(String message) {
        log.debug("[LOCAL DEBUG] Dummy Notification sent: {}", message);
        return "[LOCAL] Mock Notification Delivered: " + message;
    }
}
