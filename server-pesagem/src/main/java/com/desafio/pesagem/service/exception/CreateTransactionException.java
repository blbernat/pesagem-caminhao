package com.desafio.pesagem.service.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class CreateTransactionException extends DataIntegrityViolationException {
    public CreateTransactionException(String e) {
        super(e);
    }
}
