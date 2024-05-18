package pl.healthyprogrammer.web.entrypoint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.healthyprogrammer.core.usecase.exception.IngredientNotFoundException;
import pl.healthyprogrammer.core.usecase.exception.RecipeNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return buildResponseEntity(new ExceptionDetails(500, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<Object> handleIngredientNotFoundException(IngredientNotFoundException e) {
        return buildResponseEntity(new ExceptionDetails(404, HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<Object> handleRecipeNotFoundException(RecipeNotFoundException e) {
        return buildResponseEntity(new ExceptionDetails(404, HttpStatus.NOT_FOUND, e.getMessage(), LocalDateTime.now()));
    }

    private ResponseEntity<Object> buildResponseEntity(ExceptionDetails exceptionDetails) {
        return new ResponseEntity<>(exceptionDetails, exceptionDetails.getStatus());
    }
}
