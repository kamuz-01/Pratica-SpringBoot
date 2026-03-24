package org.Pratica_SpringBoot.Controllers;

public class CpfDuplicadoException extends RuntimeException {

    public CpfDuplicadoException(String message) {
        super(message);
    }
}