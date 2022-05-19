package br.com.safi.configuration.security.exception;

import br.com.safi.configuration.security.exception.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {


    @Autowired
    private MessageSource messageSource;

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<MethodArgumentNotValidExceptionDto> handler(MethodArgumentNotValidException exception) {
        List<MethodArgumentNotValidExceptionDto> errorDtoList = new ArrayList<>();
        exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .forEach(e -> {
                    String errorMessage = messageSource.getMessage(e, LocaleContextHolder.getLocale());
                    errorDtoList.add(new MethodArgumentNotValidExceptionDto(400, e.getField(), errorMessage));
                });

        return errorDtoList;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handler(IllegalArgumentException exception){
        return ResponseEntity.badRequest().body(new ErrorDto(400, exception.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handler(IOException exception){
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(MethodNotImplementException.class)
    public ResponseEntity<ErrorDto> handler(MethodNotImplementException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<ErrorDto> handler(DataBaseException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorDto> handler(MessagingException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(PersistDataException.class)
    public ResponseEntity<ErrorDto> handler(PersistDataException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(GetDataException.class)
    public ResponseEntity<ErrorDto> handler(GetDataException exception) {
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handler(Exception exception){
        return ResponseEntity.internalServerError().body(new ErrorDto(500, exception.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> handler(ValidationException exception){
        return ResponseEntity.badRequest().body(new ErrorDto(400, exception.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDto> handler(UsernameNotFoundException exception){
        return ResponseEntity.badRequest().body(new ErrorDto( 404, exception.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handler(UserNotFoundException exception){
        return ResponseEntity.badRequest().body(new ErrorDto( exception.getHttpStatus(), exception.getMessage()));
    }
}
