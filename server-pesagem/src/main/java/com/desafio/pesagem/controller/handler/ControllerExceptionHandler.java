package com.desafio.pesagem.controller.handler;

import com.desafio.pesagem.service.exception.CreateTransactionException;
import com.desafio.pesagem.service.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handlerResourceNotFoundException(ResourceNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Erro ao fazer a busca!");
        problem.setDetail(e.getMessage());
        problem.setType(URI.create("http://localhost:8080/api/v1/**"));
        return problem;
    }

    @ExceptionHandler(CreateTransactionException.class)
    public ProblemDetail handlerCreateTransactionException(CreateTransactionException e) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
        problem.setTitle("Erro ao salvar usuário!");
        problem.setDetail(e.getMessage());
        problem.setType(URI.create("http://localhost:8080/v1/usuarios"));
        return problem;
    }
}
