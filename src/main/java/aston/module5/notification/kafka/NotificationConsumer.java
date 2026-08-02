package aston.module5.notification.kafka;

import aston.module5.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final EmailService emailService;

    public NotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listen(String message) {
        String[] parts = message.split(":", 2);
        if (parts.length < 2) return;

        String operation = parts[0];
        String body = parts[1];

        if ("MESSAGE".equalsIgnoreCase(operation)) {
            String[] messageParts = body.split(":", 2);
            if (messageParts.length == 2) {
                String email = messageParts[0];
                String customText = messageParts[1];

                emailService.sendNotification(email, "MESSAGE:" + customText);
            }
        } else {
            emailService.sendNotification(body, operation);
        }
    }
}