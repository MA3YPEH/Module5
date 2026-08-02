package aston.module5.notification.controller;

import aston.module5.notification.dto.EmailRequestDto;
import aston.module5.notification.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendEmailDirectly(@Valid @RequestBody EmailRequestDto dto) {
        if (dto.getOperation() != null && dto.getOperation().toUpperCase().startsWith("MESSAGE:")) {
            emailService.sendNotification(dto.getEmail(), dto.getOperation());
            return ResponseEntity.ok("Произвольное сообщение успешно отправлено на почту.");
        }

        return ResponseEntity.badRequest().body("Неподдерживаемый формат операции.");
    }
}
