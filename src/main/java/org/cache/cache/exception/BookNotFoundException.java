package org.cache.cache.exception;

/**
 * Exception thrown to indicate that a book with the specified ISBN was not found in the catalog.
 * <p>
 * This exception is typically used in scenarios where a client attempts to interact with a book
 * resource that does not exist, such as retrieving details about a specific book by its ISBN.
 * <p>
 * It is handled at the controller layer to return an appropriate HTTP response status and
 * message, providing a user-friendly message for cases where the requested book is missing.
 * <p>
 * The exception includes the ISBN of the book that could not be found and forms the error message accordingly.
 *
 * @see org.catalog.exception.BookControllerAdvice#bookNotFoundHandler
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code BookNotFoundException} with a detailed message
     * indicating that a book with the specified ISBN could not be found in the catalog.
     *
     * @param isbn the ISBN of the book that was not found
     */
    public BookNotFoundException(String isbn) {
        super("The book with ISBN " + isbn + " was not found.");
    }


}
