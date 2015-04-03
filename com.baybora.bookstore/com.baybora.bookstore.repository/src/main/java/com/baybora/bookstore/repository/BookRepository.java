package com.baybora.bookstore.repository;

import java.util.List;

import com.baybora.bookstore.domain.Book;
import com.baybora.bookstore.domain.Category;
import com.baybora.bookstore.domain.criteria.BookSearchCriteria;

/**
 * Repository for working with {@link Book} domain objects
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 *
 */
public interface BookRepository {

	Book findById(long id);

	List<Book> findByCategory(Category category);

	List<Book> findRandom(int count);

	List<Book> findBooks(BookSearchCriteria bookSearchCriteria);

	void storeBook(Book book);

}
