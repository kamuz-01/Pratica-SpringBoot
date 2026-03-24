package org.Pratica_SpringBoot.GerenciamentoErros;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    private ResponseEntity<ProblemResponse> criarProblemResponse(
            HttpStatus status,
            String titulo,
            String detalhe,
            HttpServletRequest request) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.of("pt", "BR"));

        // O @Builder do Lombok permite criar o objeto de forma muito elegante
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

    // 404
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

    // 404 - Rota ou Recurso estático não encontrado (ex: favicon.ico)
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

    // 400 - Parâmetro com tipo errado na URL (Ex: "w" ou "-" no lugar de um número)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemResponse> tratarTipoArgumentoInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        log.warn("Tipo de argumento inválido em {} : {}", request.getRequestURI(), ex.getMessage());

        Object value = ex.getValue();

        String valorEnviado = (value != null) ? value.toString() : "nulo";

        Class<?> requiredType = ex.getRequiredType();

        String tipoEsperado = (requiredType != null) ? requiredType.getSimpleName() 
        : "desconhecido";

        String detalhe = String.format("O parâmetro '%s' recebeu o valor '%s', que é de um tipo inválido. O tipo correto deve ser '%s'.",
                ex.getName(), valorEnviado, tipoEsperado);

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Parâmetro de URL inválido",
                detalhe,
                request);
    }

    // 400 - Validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemResponse> tratarErroValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        log.warn("Erro de validação em {} : {}", request.getRequestURI(), ex.getMessage());

        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        String detalhe = "Um ou mais campos estão inválidos: " + String.join(", ", erros);

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                detalhe,
                request);
    }

    // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemResponse> tratarConflito(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        log.error("Conflito de dados em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "Violação de integridade na base de dados",
                request);
    }

    // 422
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemResponse> tratarRegraNegocio(
            IllegalStateException ex,
            HttpServletRequest request) {

        log.warn("Erro de regra de negócio em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Erro de regra de negócio",
                ex.getMessage(),
                request);
    }

    // 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemResponse> tratarBadRequest(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Requisição inválida em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                ex.getMessage(),
                request);
    }

    // 400 - Parte multipart obrigatória ausente
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ProblemResponse> tratarParteMultipartAusente(
            MissingServletRequestPartException ex,
            HttpServletRequest request) {

        log.warn("Parte multipart ausente em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Parte da requisição ausente",
                "A parte obrigatória '" + ex.getRequestPartName() + "' não foi enviada no multipart/form-data.",
                request);
    }

    // 400 - JSON inválido dentro da parte multipart
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemResponse> tratarCorpoNaoLegivel(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        log.warn("Corpo não legível em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido",
                "O conteúdo enviado no campo JSON não pôde ser interpretado.",
                request);
    }

    // 413 - Arquivo maior do que o limite configurado
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemResponse> tratarArquivoGrande(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {

        log.warn("Arquivo excedeu o tamanho permitido em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "Imagem muito grande",
                "A imagem de perfil ultrapassa o limite máximo de 5 MB.",
                request);
    }

    // 400 - Erro genérico de multipart
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ProblemResponse> tratarErroMultipart(
            MultipartException ex,
            HttpServletRequest request) {

        log.warn("Erro multipart em {} : {}", request.getRequestURI(), ex.getMessage());

        return criarProblemResponse(
                HttpStatus.BAD_REQUEST,
                "Erro no upload",
                "A requisição multipart/form-data está inválida.",
                request);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemResponse> tratarErroInterno(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro interno em {} ", request.getRequestURI(), ex);

        return criarProblemResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                "Ocorreu um erro inesperado",
                request);
    }
}