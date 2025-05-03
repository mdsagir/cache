package org.cache.cache.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * A controller advice for handling exceptions in the context of book-related operations.
 * Provides centralized exception handling for exceptions thrown by controllers, returning
 * appropriate HTTP responses and error messages.
 * <p>
 * This class specifically handles exceptions related to book operations, such as scenarios
 * where a book is not found, a duplicate book is attempted to be added, or validation errors
 * occur during request processing.
 */
@RestControllerAdvice
public class BookControllerAdvice {
    /**
     * Handles the {@code BookNotFoundException} when a requested book is not found in the catalog.
     * This method returns an appropriate error message and sets the HTTP status to 404 (Not Found).
     *
     * @param ex the exception instance containing details about the book that was not found
     * @return a string containing the exception message
     */
    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String bookNotFoundHandler(BookNotFoundException ex) {
        return ex.getMessage();
    }

    /**
     * Handles the {@code BookAlreadyExistsException} when attempting to add a book
     * that already exists in the catalog. This method returns an appropriate error
     * message and sets the HTTP status to 422 (Unprocessable Entity).
     *
     * @param ex the exception instance containing details about the conflicting book
     * @return a string containing the exception message
     */
    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String bookAlreadyExistsHandler(BookAlreadyExistsException ex) {
        return ex.getMessage();
    }

    /**
     * Handles validation errors arising from method argument validation failures, specifically
     * when a {@code MethodArgumentNotValidException} is thrown. This method extracts field-specific
     * error messages from the exception and maps them to their respective field names.
     * <p>
     * Responds with an HTTP status of 400 (Bad Request).
     *
     * @param ex the exception containing details about the validation failures, including field names
     *           and error messages
     * @return a map where the keys represent field names with validation errors and the values
     *         represent the corresponding error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
