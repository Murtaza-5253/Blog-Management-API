package com.mz.blog.service;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.CreateCategoryRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.dto.response.CategoryResponse;
import com.mz.blog.dto.response.PageResponse;
import com.mz.blog.entity.Author;
import com.mz.blog.entity.Category;
import com.mz.blog.exception.DuplicateResourceException;
import com.mz.blog.exception.ResourceNotFoundException;
import com.mz.blog.mapper.CategoryMapper;
import com.mz.blog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Create Category with name {}", request.getName());

        if(categoryRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        log.info("Saved Category with ID: {} & name: {}", savedCategory.getId(),savedCategory.getName());
        return categoryMapper.toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("Get Category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("category", "id", id));

        return categoryMapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getAllCategories(int page,int size,String sortBy,String sortDir){
        log.info("Fetching all Categories - page: {}, size: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch from DB
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        //convert to DTO
        List<CategoryResponse> categoryResponses = categoryPage.getContent()
                .stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<CategoryResponse>builder()
                .content(categoryResponses)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .last(categoryPage.isLast())
                .first(categoryPage.isFirst())
                .build();
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Deleting Category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
        log.info("Categiry Deleted successfully with ID: {}", id);
    }
    @Transactional(readOnly = true)
    public Category getCategoryEntityById(Long id) {
        log.info("Retrieving Author with ID: {}", id);
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", id));
    }
}
