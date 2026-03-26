package org.Pratica_SpringBoot.Services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

class UsuarioImagemStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void salvarImagemPerfilDeveRetornarNullQuandoArquivoVazio() {
        UsuarioImagemStorageService service = serviceComBaseDir(tempDir.toString());
        assertNull(service.salvarImagemPerfil(1L, new MockMultipartFile("imagem", new byte[0])));
    }

    @Test
    void salvarImagemPerfilDeveCriarArquivoNaEstruturaEsperada() throws Exception {
        byte[] conteudo = new byte[] { 1, 2, 3, 4 };
        MockMultipartFile imagem = new MockMultipartFile("imagem", "foto.png", "image/png", conteudo);
        UsuarioImagemStorageService service = serviceComBaseDir(tempDir.toString());

        String caminho = service.salvarImagemPerfil(7L, imagem);

        assertEquals(tempDir + "/7/foto-perfil.png", caminho);
        assertArrayEquals(conteudo, Files.readAllBytes(tempDir.resolve("7").resolve("foto-perfil.png")));
    }

    @Test
    void salvarImagemPerfilDeveUsarExtensaoPadraoQuandoNomeNaoTemExtensao() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagem", "foto", "image/png", new byte[] { 9 });
        UsuarioImagemStorageService service = serviceComBaseDir(tempDir.toString());

        String caminho = service.salvarImagemPerfil(8L, imagem);

        assertEquals(tempDir + "/8/foto-perfil.img", caminho);
        assertEquals(1, Files.readAllBytes(tempDir.resolve("8").resolve("foto-perfil.img")).length);
    }

    @Test
    void salvarImagemPerfilDeveLancarErroQuandoPersistenciaFalha() throws Exception {
        MockMultipartFile imagem = new MockMultipartFile("imagem", "foto.png", "image/png", new byte[] { 1 });
        Files.writeString(tempDir.resolve("arquivo-em-vez-de-pasta"), "conteudo");
        UsuarioImagemStorageService service = serviceComBaseDir(tempDir.resolve("arquivo-em-vez-de-pasta").toString());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.salvarImagemPerfil(9L, imagem));
        assertEquals("Falha ao salvar a imagem de perfil do usuário", exception.getMessage());
    }

    private UsuarioImagemStorageService serviceComBaseDir(String baseDir) {
        UsuarioImagemStorageService service = new UsuarioImagemStorageService();
        ReflectionTestUtils.setField(service, "baseDir", baseDir);
        return service;
    }
}