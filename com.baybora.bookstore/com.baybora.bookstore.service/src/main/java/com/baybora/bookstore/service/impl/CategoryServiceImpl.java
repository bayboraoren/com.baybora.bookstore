package com.baybora.bookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baybora.bookstore.domain.Category;
import com.baybora.bookstore.repository.CategoryRepository;
import com.baybora.bookstore.service.CategoryService;

/**
 * @see CategoryService
 * @author Marten Deinum
 * @author Koen Serneels
 *
 */
@Service("categoryService")
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category findById(long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public List<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	@Override
	public void addCategory(Category category) {
		categoryRepository.storeCategory(category);
	}
}
