package org.cache.cache.service;

import org.cache.cache.exception.BookAlreadyExistsException;
import org.cache.cache.exception.BookNotFoundException;
import org.cache.cache.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service class for managing books in a catalog. This class provides methods to
 * perform operations such as retrieving, adding, updating, and removing books
 * from the catalog. It also handles scenarios where a book is not found or
 * already exists.
 */
@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    Map<String, Book> bookMap = new java.util.HashMap<>();

    /**
     * Retrieves a list of all books available in the catalog.
     *
     * @return an {@code Iterable} containing all {@code Book} objects in the catalog,
     * or an empty collection if no books are available
     */
    @Cacheable(value = "bookList")
    public Iterable<Book> viewBookList() {
        var values = new ArrayList<>(bookMap.values());
        log.debug("Retrieved {} books from the catalog", values.size());
        return values;
    }


    /**
     * Retrieves the details of a book based on its ISBN.
     *
     * @param isbn the ISBN of the book whose details are to be retrieved
     * @return the {@code Book} object containing the details of the book, or {@code null} if no book is found with the given ISBN
     */
    @Cacheable(value = "book", key = "#isbn")
    public Book viewBookDetails(String isbn) {
        log.debug("Retrieving book details for ISBN: {}", isbn);
        return bookMap.computeIfAbsent(isbn, this::handleBookNotFound);
    }

    /**
     * Adds a new book to the catalog. If a book with the same ISBN already exists
     * in the catalog, an exception is thrown.
     *
     * @param book the {@code Book} object to be added to the catalog
     * @return the added {@code Book} object after successful persistence
     * @throws BookAlreadyExistsException if a book with the same ISBN already exists
     */
    @CachePut(value = "book", key = "#book.isbn()")
    @CacheEvict(value = "bookList", allEntries = true)
    public Book addBookToCatalog(Book book) {
        log.debug("Adding book to catalog: {}, ISBN: {}", book, book.isbn());
        return bookMap.compute(book.isbn(), (isbn, existingBook) -> {
            if (existingBook != null) {
                log.error("Book with ISBN {} already exists in the catalog", isbn);
                throw new BookAlreadyExistsException(isbn);
            }
            log.debug("Adding successfully book to catalog: {}, ISBN: {}", book, book.isbn());
            return book;
        });
    }

    /**
     * Removes a book from the catalog based on its ISBN.
     *
     * @param isbn the ISBN of the book to be removed from the catalog
     */
    @CacheEvict(value = {"book", "bookList"}, key = "#isbn", allEntries = true)
    public void removeBookFromCatalog(String isbn) {
        log.debug("Removing book from catalog: {}", isbn);
        var removedBook = bookMap.remove(isbn);
        if (removedBook == null) {
            handleBookNotFound(isbn);
        }
        log.debug("Book removed from catalog: {}, ISBN: {}", removedBook, isbn);
    }

    /**
     * Updates the details of an existing book in the catalog based on its ISBN.
     * If the book with the given ISBN exists, its details are updated with the provided {@code Book} object.
     *
     * @param isbn the ISBN of the book to be updated
     * @param book the {@code Book} object containing the updated details of the book
     * @return the updated {@code Book} object after successful modification, or {@code null} if no book is found with the given ISBN
     */
    @CachePut(value = "book", key = "#isbn")
    @CacheEvict(value = "bookList", allEntries = true)
    public Book editBookDetails(String isbn, Book book) {
        log.debug("Updating book details for ISBN: {}", isbn);
        return bookMap.compute(isbn, (i, existingBook) -> {
            if (existingBook != null) {
                var bookToUpdate = new Book(
                        existingBook.id(),
                        existingBook.isbn(),
                        book.title(),
                        book.author(),
                        book.price(),
                        book.publisher(),
                        existingBook.createdDate(),
                        existingBook.lastModifiedDate(),
                        existingBook.createdBy(),
                        existingBook.lastModifiedBy(),
                        existingBook.version());
                bookMap.put(isbn, bookToUpdate);
                log.debug("Book details updated successfully for ISBN: {}", isbn);
                return bookToUpdate;
            }
            handleBookNotFound(isbn);
            return book;
        });
    }

    /**
     * Handles the case where a book with the specified ISBN is not found in the catalog.
     * Logs an error message and throws a {@code BookNotFoundException}.
     *
     * @param isbn the ISBN of the book that could not be found
     * @return never returns, as this method always throws a {@code BookNotFoundException}
     * @throws BookNotFoundException always thrown to indicate the book was not found in the catalog
     */
    private Book handleBookNotFound(String isbn) {
        log.error("Book with ISBN {} not found in the catalog", isbn);
        throw new BookNotFoundException(isbn);
    }
}
