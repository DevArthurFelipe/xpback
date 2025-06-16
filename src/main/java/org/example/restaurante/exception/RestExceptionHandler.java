package org.example.restaurante.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handler global de exceções para a aplicação REST.
 * Captura exceções específicas lançadas pelos controllers e as converte
 * em respostas HTTP padronizadas.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Captura exceções ResourceNotFoundException e retorna uma resposta HTTP 404 Not Found.
     * @param ex A exceção capturada.
     * @return um ResponseEntity com status 404 e a mensagem da exceção no corpo.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Captura exceções InvalidMasterKeyException e retorna uma resposta HTTP 401 Unauthorized.
     * @param ex A exceção capturada.
     * @return um ResponseEntity com status 401 e a mensagem da exceção no corpo.
     */
    @ExceptionHandler(InvalidMasterKeyException.class)
    public ResponseEntity<Object> handleInvalidMasterKeyException(InvalidMasterKeyException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Captura exceções IllegalArgumentException e retorna uma resposta HTTP 400 Bad Request.
     * Ideal para erros de validação de negócio (ex: tentar salvar uma comanda sem itens).
     * @param ex A exceção capturada.
     * @return um ResponseEntity com status 400 e a mensagem da exceção no corpo.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}