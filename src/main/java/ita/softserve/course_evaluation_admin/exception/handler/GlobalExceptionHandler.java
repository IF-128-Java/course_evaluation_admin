package ita.softserve.course_evaluation_admin.exception.handler;

import ita.softserve.course_evaluation_admin.exception.dto.GenericExceptionResponse;
import ita.softserve.course_evaluation_admin.exception.exceptions.NotEmptyGroupException;
import ita.softserve.course_evaluation_admin.exception.exceptions.WrongIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({WrongIdException.class})
    public ResponseEntity<GenericExceptionResponse> handleEntityNotFoundException(WrongIdException exception) {
        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<GenericExceptionResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotEmptyGroupException.class})
    public ResponseEntity<GenericExceptionResponse> handleEntityNotFoundException(NotEmptyGroupException exception) {
        GenericExceptionResponse dto = GenericExceptionResponse.builder()
                .message(HttpStatus.BAD_REQUEST.name())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}

