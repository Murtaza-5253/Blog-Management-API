package com.mz.blog.mapper;

import com.mz.blog.dto.request.CreateAuthorRequest;
import com.mz.blog.dto.request.CreateCategoryRequest;
import com.mz.blog.dto.request.UpdateAuthorRequest;
import com.mz.blog.dto.response.AuthorResponse;
import com.mz.blog.dto.response.CategoryResponse;
import com.mz.blog.entity.Author;
import com.mz.blog.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request){
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }


    public CategoryResponse toResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .articleCount((long) category.getArticles().size())
                .createdOn(category.getCreatedOn())
                .build();
    }
}
