package com.baybora.bookstore.service;

import java.util.List;

import com.baybora.bookstore.domain.Category;

/**
 * Contract for services that work with an {@link Category}.
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
public interface CategoryService {

	Category findById(long id);

	List<Category> findAll();

	void addCategory(Category category);

}
