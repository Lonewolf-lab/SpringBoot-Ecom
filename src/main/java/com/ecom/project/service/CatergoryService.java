package com.ecom.project.service;


import com.ecom.project.payload.CategoryDTO;
import com.ecom.project.payload.CategoryResponse;

import java.util.List;

public interface CatergoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder );
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}
