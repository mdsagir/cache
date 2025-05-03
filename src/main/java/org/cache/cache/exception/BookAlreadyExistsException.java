package org.cache.cache.exception;

/**
 * Exception thrown to indicate that a book with the specified ISBN already exists in the catalog.
 * <p>
 * This exception is typically used in operations where the addition of a book is attempted,
 * such as in the {@code addBookToCatalog} method in a service layer, and a conflict is
 * detected because a book with the same ISBN is already present.
 * <p>
 * It is handled at the controller layer to return an appropriate HTTP response
 * status and message, ensuring proper error handling in API operations.
 *
 * @see org.catalog.service.BookService#addBookToCatalog
 * @see BookControllerAdvice#bookAlreadyExistsHandler
 */
public class BookAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new {@code BookAlreadyExistsException} with a detailed message
     * indicating that a book with the specified ISBN already exists in the catalog.
     *
     * @param isbn the ISBN of the book that caused the exception
     */
    public BookAlreadyExistsException(String isbn) {
        super("A book with ISBN " + isbn + " already exists.");
    }

}
