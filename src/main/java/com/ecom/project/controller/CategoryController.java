package com.ecom.project.controller;

import com.ecom.project.model.Category;
import com.ecom.project.service.CatergoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private CatergoryService catergoryService;

    public CategoryController(CatergoryService catergoryService) {
        this.catergoryService = catergoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = catergoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        catergoryService.createCategory(category);
        return new ResponseEntity<>("Category added succesfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryId){
        try {
            String status = catergoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status , HttpStatus.OK);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason() , e.getStatusCode());
        }
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        try{
            Category addedCategory = catergoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>("Category with category Id" + categoryId, HttpStatus.OK);
        }catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason() , e.getStatusCode());
        }
    }

}
