package com.gdgku.study.backend.common.profile;

import com.gdgku.study.backend.notification.NotificationService;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final Environment environment;
    private final NotificationService notificationService;

    public ProfileController(Environment environment, NotificationService notificationService) {
        this.environment = environment;
        this.notificationService = notificationService;
    }

    @GetMapping
    public Map<String, Object> getProfileInfo() {
        Map<String, Object> response = new HashMap<>();
        String[] activeProfiles = environment.getActiveProfiles();
        String currentProfile = activeProfiles.length > 0 ? activeProfiles[0] : "default";

        response.put("activeProfile", currentProfile);
        response.put("notificationResult", notificationService.sendNotification("Hello from " + currentProfile));
        return response;
    }
}
