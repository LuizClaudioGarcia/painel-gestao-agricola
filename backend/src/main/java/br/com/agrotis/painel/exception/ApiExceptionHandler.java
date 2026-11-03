package br.com.agrotis.painel.exception;

import br.com.agrotis.painel.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String EXEMPLO_UUID = "00000000-0000-7000-8000-000000000021";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(new ErroResponse("Dados inválidos", erros));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regra(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(new ErroResponse(ex.getMessage(), List.of()));
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<ErroResponse> naoEncontrado(NaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(ex.getMessage(), List.of()));
    }

    @ExceptionHandler(InscricaoDuplicadaException.class)
    public ResponseEntity<ErroResponse> inscricaoDuplicada(InscricaoDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponse(ex.getMessage(), List.of()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> conflito(DataIntegrityViolationException ex) {
        if (inscricaoRepetida(ex)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErroResponse(InscricaoDuplicadaException.MENSAGEM, List.of()));
        }
        log.error("Conflito ao gravar", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse("Os dados batem com um cadastro que já existe.", List.of()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        if (UUID.class.equals(ex.getRequiredType())) {
            return resposta(HttpStatus.BAD_REQUEST, "O " + ex.getName() + " precisa ser um UUID. Exemplo: " + EXEMPLO_UUID);
        }
        if (OffsetDateTime.class.equals(ex.getRequiredType())) {
            return resposta(HttpStatus.BAD_REQUEST, "O parâmetro " + ex.getName() + " precisa ser uma data com fuso. Exemplo: 2026-01-01T00:00:00Z");
        }
        return resposta(HttpStatus.BAD_REQUEST, "O valor de " + ex.getName() + " é inválido.");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> parametroObrigatorio(MissingServletRequestParameterException ex) {
        if ("quantidadeMinima".equals(ex.getParameterName())) {
            return resposta(HttpStatus.BAD_REQUEST, "Informe quantidadeMinima, um inteiro a partir de 1.");
        }
        return resposta(HttpStatus.BAD_REQUEST, "Informe o parâmetro " + ex.getParameterName() + ".");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException ex) {
        if (ex.getMostSpecificCause() instanceof InvalidFormatException formato) {
            String campo = nomeDoCampo(formato);
            if (UUID.class.equals(formato.getTargetType())) {
                return resposta(HttpStatus.BAD_REQUEST, "O campo " + campo + " precisa ser um UUID. Exemplo: " + EXEMPLO_UUID);
            }
            if (OffsetDateTime.class.equals(formato.getTargetType())) {
                return resposta(HttpStatus.BAD_REQUEST, "O campo " + campo + " precisa ser uma data com fuso. Exemplo: 2026-07-01T08:00:00Z");
            }
        }
        return resposta(HttpStatus.BAD_REQUEST, "Não consegui ler o JSON. Confira aspas, vírgulas, datas e UUIDs.");
    }

    private boolean inscricaoRepetida(DataIntegrityViolationException ex) {
        String texto = ex.getMostSpecificCause().getMessage();
        return texto != null && texto.contains("uk_entidade_inscricao_fiscal");
    }

    private String nomeDoCampo(InvalidFormatException formato) {
        String nome = formato.getPath().stream()
                .map(referencia -> referencia.getFieldName() != null
                        ? referencia.getFieldName()
                        : "[" + referencia.getIndex() + "]")
                .collect(Collectors.joining("."));
        return nome.isBlank() ? "informado" : nome;
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(mensagem, List.of()));
    }
}
