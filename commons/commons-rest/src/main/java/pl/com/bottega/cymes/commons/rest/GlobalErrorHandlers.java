package pl.com.bottega.cymes.commons.rest;

import jakarta.persistence.EntityNotFoundException;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
class GlobalErrorHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<GlobalError> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.badRequest().body(new GlobalError(ex.getMessage()));
    }

    @ExceptionHandler(PSQLException.class)
    ResponseEntity<GlobalError> handlePSQLException(PSQLException ex) throws PSQLException {
       if(ex.getSQLState().equals(PSQLState.FOREIGN_KEY_VIOLATION.getState())) {
           return new ResponseEntity<>(new GlobalError(ex.getMessage()), HttpStatus.NOT_FOUND);
       } else if(ex.getSQLState().equals(PSQLState.UNIQUE_VIOLATION.getState())) {
           return new ResponseEntity<>(new GlobalError(ex.getMessage()), HttpStatus.CONFLICT);
       }
       throw ex;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        return ResponseEntity.badRequest().body(new Errors(
            ex.getBindingResult().getFieldErrors().stream()
                .map(GlobalErrorHandlers::toError)
                .collect(Collectors.toList())
        ));
    }

    private static Error toError(FieldError fieldError) {
        return new Error(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
