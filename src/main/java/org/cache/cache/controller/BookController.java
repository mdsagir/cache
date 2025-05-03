package org.cache.cache.controller;

import org.cache.cache.model.Book;
import org.cache.cache.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code BookController} class is a REST controller responsible for managing
 * operations related to books in the catalog. It provides endpoints to retrieve,
 * add, update, and delete books.
 * <p>
 * The controller interacts with the {@code BookService} to perform these operations
 * and logs actions for debugging purposes.
 */
@RestController
@RequestMapping("books")
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;

    /**
     * Constructs a {@code BookController} with the given {@code BookService}.
     *
     * @param bookService the service used to manage book operations in the catalog
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Retrieves a list of all books available in the catalog.
     *
     * @return an {@code Iterable} containing all {@code Book} objects from the catalog,
     * or an empty collection if no books are available
     */
    @GetMapping
    public Iterable<Book> get() {
        log.debug("Retrieving all books from the catalog");
        return bookService.viewBookList();
    }

    /**
     * Retrieves the details of a book based on its ISBN.
     *
     * @param isbn the ISBN of the book whose details are to be retrieved
     * @return the {@code Book} object containing the details of the book, or {@code null} if no book is found with the given ISBN
     */
    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        log.debug("Retrieving book details for ISBN: {}", isbn);
        return bookService.viewBookDetails(isbn);
    }

    /**
     * Adds a new book to the catalog. The added book is returned upon successful persistence.
     *
     * @param book the {@code Book} object to be added to the catalog
     * @return the added {@code Book} object after successful persistence
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(@RequestBody Book book) {
        log.debug("Adding book to catalog: {}", book);
        return bookService.addBookToCatalog(book);
    }

    /**
     * Deletes a book from the catalog based on its ISBN.
     * This operation removes the book entry if it exists in the catalog.
     *
     * @param isbn the ISBN of the book to be deleted from the catalog
     */

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    /**
     * Updates the details of an existing book in the catalog based on its ISBN.
     * If the book with the given ISBN exists, its details are updated with the provided {@code Book} object.
     *
     * @param isbn the ISBN of the book to be updated. This is used to identify the book in the catalog.
     * @param book the {@code Book} object containing the updated details of the book to be persisted.
     * @return the updated {@code Book} object after successful modification.
     */
    @PutMapping("{isbn}")
    public Book put(@PathVariable String isbn, @RequestBody Book book) {
        return bookService.editBookDetails(isbn, book);
    }
}
