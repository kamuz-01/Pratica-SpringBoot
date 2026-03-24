package org.Pratica_SpringBoot.GerenciamentoErros;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.Pratica_SpringBoot.Docs.ProblemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ManipuladorExcecoesGlobais {

    private static final Logger log = LoggerFactory.getLogger(ManipuladorExcecoesGlobais.class);

    // -------------------------------------------------------------------------
    // Exceção de domínio: CPF duplicado
    // Centralizada aqui para que esta seja a única classe de erros do sistema.
    // Nos services, importe como:
    //   import org.Pratica_SpringBoot.GerenciamentoErros.ManipuladorExcecoesGlobais;
    //   throw new ManipuladorExcecoesGlobais.CpfDuplicadoException("mensagem");
    // -------------------------------------------------------------------------

    public static class CpfDuplicadoException extends RuntimeException {
        public CpfDuplicadoException(String message) {
            super(message);
        }
    }

    // -------------------------------------------------------------------------
    // Método auxiliar central — monta o ProblemResponse padronizado
    // -------------------------------------------------------------------------

    private ResponseEntity<ProblemResponse> criarProblemResponse(
            HttpStatus status,
            String titulo,
            String detalhe,
            HttpServletRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.of("pt", "BR"));

        ProblemResponse problem = ProblemResponse.builder()
                .status(status.value())
                .titulo(titulo)
                .detalhe(detalhe)
                .instancia(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(formatter))
                .metodo(request.getMethod())
                .build();

        return ResponseEntity.status(status).body(problem);
    }

    // -------------------------------------------------------------------------
    // 404 — Recurso de negócio não encontrado
    // -------------------------------------------------------------------------

    @ExceptionHandler(RecursosNaoEncontradosException.class)
    public ResponseEntity<ProblemResponse> tratarNaoEncontrado(
            RecursosNaoEncontradosException ex,
            HttpServletRequest request) {

        log.warn("Recurso não encontrado: {} - {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getMessage(),
                request);
    }

    // -------------------------------------------------------------------------
    // 404 — NoSuchElementException (lançada nos services quando findById falha)
    // -------------------------------------------------------------------------

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemResponse> tratarElementoNaoEncontrado(
            NoSuchElementException ex,
            HttpServletRequest request) {

        log.warn("Elemento não encontrado: {} - {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getMessage(),
                request);
    }

    // -------------------------------------------------------------------------
    // 404 — Rota ou recurso estático inexistente (ex: favicon.ico)
    // -------------------------------------------------------------------------

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemResponse> tratarRotaNaoEncontrada(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        log.warn("Rota ou recurso não encontrado: {}", request.getRequestURI());

        return criarProblemResponse(
                HttpStatus.NOT_FOUND,
                "Caminho não encontrado",
                "A URL ou recurso solicitado ('" + request.getRequestURI() + "') não existe neste servidor.",
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — Parâmetro de URL com tipo inválido (ex: letra onde se espera número)
    // -------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemResponse> tratarTipoArgumentoInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        log.warn("Tipo de argumento inválido em {}: {}", request.getRequestURI(), ex.getMessage());

        Object value = ex.getValue();
        String valorEnviado = (value != null) ? value.toString() : "nulo";

        Class<?> requiredType = ex.getRequiredType();
        String tipoEsperado = (requiredType != null) ? requiredType.getSimpleName() : "desconhecido";

        String detalhe = String.format(
                "O parâmetro '%s' recebeu o valor '%s', que é de um tipo inválido. O tipo correto deve ser '%s'.",
                ex.getName(), valorEnviado, tipoEsperado);

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Parâmetro de URL inválido",
                detalhe,
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — Falha de validação nos campos do body (@Valid / @Validated)
    // -------------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemResponse> tratarErroValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Erro de validação em {}: {}", request.getRequestURI(), ex.getMessage());

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        boolean cpfInvalido = ex.getBindingResult().getFieldErrors().stream()
                .anyMatch(e -> "cpf".equals(e.getField()));

        String titulo = cpfInvalido ? "CPF inválido" : "Erro de validação";
        String detalhe = cpfInvalido
                ? "O CPF informado é inválido. Campos com problema: " + String.join(", ", erros)
                : "Um ou mais campos estão inválidos: " + String.join(", ", erros);

        return criarProblemResponse(HttpStatus.BAD_REQUEST, titulo, detalhe, request);
    }

    // -------------------------------------------------------------------------
    // 409 — CPF duplicado
    // -------------------------------------------------------------------------

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<ProblemResponse> tratarCpfDuplicado(
            CpfDuplicadoException ex,
            HttpServletRequest request) {

        log.warn("CPF duplicado em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.CONFLICT,
                "CPF duplicado",
                ex.getMessage(),
                request);
    }

    // -------------------------------------------------------------------------
    // 409 — Violação de integridade no banco (unique constraint, FK, etc.)
    // -------------------------------------------------------------------------

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemResponse> tratarConflito(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.error("Conflito de dados em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "Já existe um registro com os dados informados.",
                request);
    }

    // -------------------------------------------------------------------------
    // 422 — Regra de negócio violada (IllegalStateException)
    // -------------------------------------------------------------------------

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemResponse> tratarRegraNegocio(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.warn("Erro de regra de negócio em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Erro de regra de negócio",
                ex.getMessage(),
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — Argumento inválido na lógica de negócio (IllegalArgumentException)
    // -------------------------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemResponse> tratarBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Requisição inválida em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                ex.getMessage(),
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — Parte multipart obrigatória ausente
    // -------------------------------------------------------------------------

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ProblemResponse> tratarParteMultipartAusente(
            MissingServletRequestPartException ex,
            HttpServletRequest request) {

        log.warn("Parte multipart ausente em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Parte da requisição ausente",
                "A parte obrigatória '" + ex.getRequestPartName() + "' não foi enviada no multipart/form-data.",
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — JSON inválido ou ilegível no body
    // -------------------------------------------------------------------------

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemResponse> tratarCorpoNaoLegivel(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("Corpo não legível em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido",
                "O conteúdo enviado no campo JSON não pôde ser interpretado.",
                request);
    }

    // -------------------------------------------------------------------------
    // 413 — Arquivo maior que o limite configurado (5 MB)
    // -------------------------------------------------------------------------

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemResponse> tratarArquivoGrande(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {

        log.warn("Arquivo excedeu o tamanho permitido em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Imagem muito grande",
                "A imagem de perfil ultrapassa o limite máximo de 5 MB.",
                request);
    }

    // -------------------------------------------------------------------------
    // 400 — Erro genérico de multipart
    // -------------------------------------------------------------------------

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ProblemResponse> tratarErroMultipart(
            MultipartException ex,
            HttpServletRequest request) {

        log.warn("Erro multipart em {}: {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Erro no upload",
                "A requisição multipart/form-data está inválida.",
                request);
    }

    // -------------------------------------------------------------------------
    // 500 — Qualquer exceção não tratada explicitamente
    // -------------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemResponse> tratarErroInterno(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro interno em {}", request.getRequestURI(), ex);

        return criarProblemResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado.",
                request);
    }
}