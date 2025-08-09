package com.projetospring.todolist.errors;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // Anotação que indica que esta classe é um controlador de exceções
public class ExceptionHandlerController {
    

    @ExceptionHandler(HttpMessageNotReadableException.class) // Captura exceções do tipo HttpMessageNotReadableException
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // Retorna uma resposta com status 400 (Bad Request) e uma mensagem de erro
        return ResponseEntity.status(400).body(e.getMostSpecificCause().getMessage());    
    }
}
