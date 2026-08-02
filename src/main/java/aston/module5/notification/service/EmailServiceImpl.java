package aston.module5.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(String toEmail, String operation) {

        if (toEmail == null || operation == null) {
            throw new IllegalArgumentException("Email и тип операции не могут быть пустыми (null)");
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Уведомление от системы");

        String opUpper = operation.toUpperCase();

        if ("CREATE".equals(opUpper)) {
            message.setText("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
        } else if ("DELETE".equals(opUpper)) {
            message.setText("Здравствуйте! Ваш аккаунт был удалён.");
        } else if ("UPDATE".equals(opUpper)) {
            message.setText("Здравствуйте! Ваш аккаунт был изменен.");
        } else if (opUpper.startsWith("MESSAGE:")) {
            String customText = operation.length() > 8 ? operation.substring(8) : "";
            message.setText(customText);
        } else {
            message.setText(operation);
        }

        mailSender.send(message);
    }
}
