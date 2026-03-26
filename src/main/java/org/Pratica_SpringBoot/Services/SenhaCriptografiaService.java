package org.Pratica_SpringBoot.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SenhaCriptografiaService {

    private static final int BCRYPT_STRENGTH = 10;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCRYPT_STRENGTH);

    public String criptografar(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("A senha não pode ser vazia");
        }

        return passwordEncoder.encode(senha);
    }
}