package aston.module5.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Модель запроса для прямой отправки сообщения")
public class EmailRequestDto {

    @NotBlank(message = "Email получателя не может быть пустым")
    @Email(message = "Неверный формат email адреса")
    @Schema(description = "Электронная почта адресата", example = "ivan@mail.ru")
    private String email;

    @NotBlank(message = "Тип операции (CREATE, DELETE, UPDATE) обязателен")
    @Schema(description = "Шаблонная операция (CREATE, DELETE, UPDATE) или произвольное сообщение с префиксом MESSAGE:",
            example = "MESSAGE:Привет! Это тестовое письмо.")
    private String operation;

    public EmailRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}