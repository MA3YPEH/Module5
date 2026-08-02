package aston.module5.notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailRequestDto {

    @NotBlank(message = "Email получателя не может быть пустым")
    @Email(message = "Неверный формат email адреса")
    private String email;

    @NotBlank(message = "Тип операции (CREATE, DELETE, UPDATE) обязателен")
    private String operation;

    public EmailRequestDto() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
}