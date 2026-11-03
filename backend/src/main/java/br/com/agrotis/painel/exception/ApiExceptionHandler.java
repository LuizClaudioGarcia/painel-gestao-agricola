package br.com.agrotis.painel.exception;

import br.com.agrotis.painel.dto.ErroCampo;
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

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final String DADOS_INVALIDOS = "Dados inválidos";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> dadosInvalidos(MethodArgumentNotValidException ex) {
        List<ErroCampo> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroCampo(erro.getField(), erro.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest().body(new ErroResponse(DADOS_INVALIDOS, erros));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> regra(RegraNegocioException ex) {
        if (ex.getCampo() != null) {
            return campoInvalido(ex.getCampo(), ex.getMessage());
        }
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage());
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
                .body(new ErroResponse("Cadastro duplicado", List.of()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tipoInvalido(MethodArgumentTypeMismatchException ex) {
        return campoInvalido(ex.getName(), detalheDoTipo(ex.getRequiredType()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> parametroObrigatorio(MissingServletRequestParameterException ex) {
        return campoInvalido(ex.getParameterName(), "ausente");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido(HttpMessageNotReadableException ex) {
        if (ex.getMostSpecificCause() instanceof InvalidFormatException formato) {
            return campoInvalido(nomeDoCampo(formato), detalheDoTipo(formato.getTargetType()));
        }
        return resposta(HttpStatus.BAD_REQUEST, "Json inválido");
    }

    private boolean inscricaoRepetida(DataIntegrityViolationException ex) {
        String texto = ex.getMostSpecificCause().getMessage();
        return texto != null && texto.contains("uk_entidade_inscricao_fiscal");
    }

    private String nomeDoCampo(InvalidFormatException formato) {
        StringBuilder nome = new StringBuilder();
        for (var referencia : formato.getPath()) {
            if (referencia.getFieldName() != null) {
                if (!nome.isEmpty()) {
                    nome.append('.');
                }
                nome.append(referencia.getFieldName());
            } else {
                nome.append('[').append(referencia.getIndex()).append(']');
            }
        }
        return nome.isEmpty() ? null : nome.toString();
    }

    private String detalheDoTipo(Class<?> tipo) {
        if (UUID.class.equals(tipo)) {
            return "UUID inválido";
        }
        if (OffsetDateTime.class.equals(tipo)) {
            return "data inválida";
        }
        return "valor inválido";
    }

    private ResponseEntity<ErroResponse> campoInvalido(String campo, String detalhe) {
        if (campo == null || campo.isBlank()) {
            return resposta(HttpStatus.BAD_REQUEST, detalhe);
        }
        return ResponseEntity.badRequest()
                .body(new ErroResponse(DADOS_INVALIDOS, List.of(new ErroCampo(campo, detalhe))));
    }

    private ResponseEntity<ErroResponse> resposta(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(mensagem, List.of()));
    }
}
