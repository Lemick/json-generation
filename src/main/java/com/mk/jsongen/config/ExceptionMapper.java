package com.mk.jsongen.config;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@Slf4j
public class ExceptionMapper extends ResponseEntityExceptionHandler {


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectNode> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> JsonNodeFactory.instance.objectNode()
                        .put("field", fieldError.getField())
                        .put("error", fieldError.getDefaultMessage())
                ).collect(Collectors.toList());

        return new ResponseEntity<>(errors, headers, status);
    }
}
