package org.Pratica_SpringBoot.Controllers;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        List<Map<String, String>> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorMap)
                .collect(Collectors.toList());

        boolean cpfInvalid = exception.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "cpf".equals(error.getField()));

        HttpStatus status = cpfInvalid ? HttpStatus.BAD_REQUEST : HttpStatus.BAD_REQUEST;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", cpfInvalid ? "CPF inválido" : "Erro de validação");
        body.put("message", cpfInvalid
                ? "O CPF informado é inválido"
                : "A requisição contém campos inválidos");
        body.put("fieldErrors", errors);

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> handleCpfDuplicado(CpfDuplicadoException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "CPF duplicado");
        body.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflito de integridade");
        body.put("message", "Já existe um registro com os dados informados");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    private Map<String, String> toErrorMap(FieldError fieldError) {
        Map<String, String> error = new LinkedHashMap<>();
        error.put("field", fieldError.getField());
        error.put("message", fieldError.getDefaultMessage());
        return error;
    }
}