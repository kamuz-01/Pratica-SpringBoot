package org.Pratica_SpringBoot.Services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioImagemStorageService {

    @Value("${app.usuario.imagens-base-dir:imagens-usuarios}")
    private String baseDir;

    public String salvarImagemPerfil(Long idUsuario, MultipartFile imagemPerfil) {
        if (imagemPerfil == null || imagemPerfil.isEmpty()) {
            return null;
        }

        String extensao = obterExtensao(imagemPerfil.getOriginalFilename());
        String nomeArquivo = "foto-perfil" + extensao;

        Path diretorioUsuario = Paths.get(baseDir, String.valueOf(idUsuario));
        Path destinoArquivo = diretorioUsuario.resolve(nomeArquivo);

        try {
            Files.createDirectories(diretorioUsuario);
            try (InputStream inputStream = imagemPerfil.getInputStream()) {
                Files.copy(inputStream, destinoArquivo, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Falha ao salvar a imagem de perfil do usuário", exception);
        }

        return baseDir + "/" + idUsuario + "/" + nomeArquivo;
    }

    private String obterExtensao(String nomeArquivoOriginal) {
        if (nomeArquivoOriginal == null || !nomeArquivoOriginal.contains(".")) {
            return ".img";
        }

        int indicePonto = nomeArquivoOriginal.lastIndexOf('.');
        return nomeArquivoOriginal.substring(indicePonto);
    }
}