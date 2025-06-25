package com.ecom.project.service;

import com.ecom.project.model.Category;

import java.util.List;

public interface CatergoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);

    String deleteCategory(long categoryId);

    Category updateCategory(Category category, Long categoryId);
}
