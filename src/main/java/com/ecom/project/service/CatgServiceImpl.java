package com.ecom.project.service;

import com.ecom.project.exceptions.APIexception;
import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Category;
import com.ecom.project.payload.CategoryDTO;
import com.ecom.project.payload.CategoryResponse;
import com.ecom.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CatgServiceImpl implements CatergoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIexception("No categories created till now");

        List<CategoryDTO> categoryDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

        CategoryResponse categoriesResponse = new CategoryResponse();
        categoriesResponse.setContent(categoryDTOS);
        return categoriesResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryfromDB = categoryRepository.findByCategoryName((category.getCategoryName()));
        if(categoryfromDB != null)
            throw new APIexception("Category with name " + categoryDTO.getCategoryName() + " already exists");
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);

    }

    @Override
    public CategoryDTO deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));


        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }


}
