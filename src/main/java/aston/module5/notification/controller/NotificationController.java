package aston.module5.notification.controller;

import aston.module5.notification.dto.EmailRequestDto;
import aston.module5.notification.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Уведомления", description = "Прямая отправка кастомных писем на почту")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/message")
    @Operation(summary = "Отправить email сообщение", description = "Принимает адрес и текст с префиксом MESSAGE:")
    @ApiResponse(responseCode = "200", description = "Письмо успешно отправлено на SMTP-сервер")
    public ResponseEntity<String> sendEmailDirectly(@Valid @RequestBody EmailRequestDto dto) {
        if (dto.getOperation() != null && dto.getOperation().toUpperCase().startsWith("MESSAGE:")) {
            emailService.sendNotification(dto.getEmail(), dto.getOperation());
            return ResponseEntity.ok("Произвольное сообщение успешно отправлено на почту.");
        }

        return ResponseEntity.badRequest().body("Неподдерживаемый формат операции.");
    }
}
