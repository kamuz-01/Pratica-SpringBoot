package org.Pratica_SpringBoot.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SenhaCriptografiaServiceTest {

    private final SenhaCriptografiaService service = new SenhaCriptografiaService();

    @Test
    void criptografarDeveRetornarHashBCryptValido() {
        String hash = service.criptografar("senhaSegura123");

        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
        assertTrue(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches("senhaSegura123", hash));
    }

    @Test
    void criptografarDeveGerarResultadosDiferentesParaMesmaSenha() {
        String primeiro = service.criptografar("senhaSegura123");
        String segundo = service.criptografar("senhaSegura123");

        assertNotEquals(primeiro, segundo);
    }

    @Test
    void criptografarDeveLancarParaSenhaVazia() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.criptografar("   "));
        assertEquals("A senha não pode ser vazia", exception.getMessage());
    }
}