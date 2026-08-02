package aston.module5.notification.service;

public interface EmailService {
    void sendNotification(String toEmail, String operation);
}