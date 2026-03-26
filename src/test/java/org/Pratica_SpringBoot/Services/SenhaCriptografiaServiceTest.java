package org.Pratica_SpringBoot.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SenhaCriptografiaServiceTest {

    private final SenhaCriptografiaService service = new SenhaCriptografiaService();

    @Test
    void criptografarDeveRetornarHashComSaltSeparadoPorDoisPontos() {
        String hash = service.criptografar("senhaSegura123");

        assertEquals(2, hash.split(":").length);
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