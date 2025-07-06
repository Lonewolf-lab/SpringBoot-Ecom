package com.ecom.project.repositories;

import com.ecom.project.model.Category;
import com.ecom.project.payload.CategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
}
