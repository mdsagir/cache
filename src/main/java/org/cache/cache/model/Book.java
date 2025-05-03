package org.cache.cache.model;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a book entity with details such as ISBN, title, author, publisher, and pricing information.
 * The {@code Book} record is immutable and intended to encapsulate all necessary data to define a book.
 * It also maintains audit information for tracking creation and modification details alongside versioning.
 *
 * @param id               the unique identifier for the book
 * @param isbn             the International Standard Book Number (ISBN) of the book
 * @param title            the title of the book
 * @param author           the author of the book
 * @param price            the price of the book
 * @param publisher        the publisher of the book
 * @param createdDate      the date and time when the book was created
 * @param lastModifiedDate the date and time when the book was last modified
 * @param createdBy        the user or system that created the book record
 * @param lastModifiedBy   the user or system that last modified the book record
 * @param version          the version number of the book record, used for tracking changes
 */
public record Book(
        Long id,
        String isbn,
        String title,
        String author,
        Double price,
        String publisher,
        Instant createdDate,
        Instant lastModifiedDate,
        String createdBy,
        String lastModifiedBy,
        int version
) implements Serializable {
    /**
     * Creates a new instance of the {@code Book} record with the specified parameters,
     * initializing only the mandatory fields while other fields are left as default values.
     *
     * @param isbn      the International Standard Book Number of the book
     * @param title     the title of the book
     * @param author    the author of the book
     * @param price     the price of the book
     * @param publisher the publisher of the book
     * @return a new {@code Book} instance with the specified details
     */
    public static Book of(String isbn, String title, String author, Double price, String publisher) {
        return new Book(null, isbn, title, author, price, publisher, null, null, null, null, 0);
    }
}
