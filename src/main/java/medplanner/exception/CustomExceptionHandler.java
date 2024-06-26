package medplanner.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return buildResponseEntity(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityExceptions(DataIntegrityViolationException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String campo = "Campo inválido ";
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException exception = (InvalidFormatException) cause;
            campo += exception.getPathReference();
        }
        return buildResponseEntity(campo, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CPFInvalidoException.class)
    public ResponseEntity<String> handleCPFInvalido() {
        return buildResponseEntity("CPF inválido", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, List<String>>> buildResponseEntity(Map<String, List<String>> errors, HttpStatus status) {
        return new ResponseEntity<>(errors, status);
    }

    private ResponseEntity<String> buildResponseEntity(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
