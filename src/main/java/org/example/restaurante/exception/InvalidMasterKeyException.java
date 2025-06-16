package org.example.restaurante.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando uma operação sensível é tentada com uma chave mestra inválida.
 * Resulta em uma resposta HTTP 401 Unauthorized.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidMasterKeyException extends RuntimeException {
    public InvalidMasterKeyException(String message) {
        super(message);
    }
}