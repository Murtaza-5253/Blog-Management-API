package com.mz.blog.controller;

import com.mz.blog.dto.request.CreateCategoryRequest;
import com.mz.blog.dto.response.CategoryResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "APIs for managing article categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Cretae a new category", description = "Creates a new article category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request)
    {
        log.info("Creating new category {}", request.getName());
        CategoryResponse categoryResponse = categoryService.createCategory(request);
        return new ResponseEntity<>(categoryResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable long id){
        log.info("Getting category by id {}", id);
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ){
        log.info("Getting all categories - page: {}, size: {}",page,size);
        PageResponse<CategoryResponse> response = categoryService.getAllCategories(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryResponse> deleteCategory(@PathVariable long id){
        log.info("Deleting category by id {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
